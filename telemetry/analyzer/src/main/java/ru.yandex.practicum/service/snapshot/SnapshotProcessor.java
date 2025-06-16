package ru.yandex.practicum.service.snapshot;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.KafkaProperties;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.mapper.ActionMapper;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.ScenarioRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor {
    @GrpcClient("hub-router")
    private HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    private final KafkaProperties kafkaProperties;
    private final SnapshotKafkaConsumer consumer;
    private final ScenarioRepository scenarioRepository;
    private final ActionMapper actionMapper;

    public void start() {
        try {
            consumer.subscribe(kafkaProperties.getHub().getTopic());

            while (true) {
                consumer.read(this::handleRecord);
            }

        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                consumer.commit();

            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
            }
        }
    }

    public void handleRecord(SensorsSnapshotAvro avro) {
        log.info("Получен snapshot {}", avro);
        List<Scenario> scenarios = scenarioRepository.findByHubId(avro.getHubId());
        Map<String, SensorStateAvro> sensorStates = avro.getSensorsState();
        scenarios = scenarios.stream()
                .filter(scenario -> checkConditions(scenario, sensorStates))
                .toList();

        scenarios.forEach(this::handleActions);
        log.info("Snapshot {} успешно отработан", avro);
    }

    private boolean checkConditions(Scenario scenario, Map<String, SensorStateAvro> sensorStates) {
        List<Condition> conditions = scenario.getConditions();
        return conditions.stream()
                .allMatch(condition -> checkCondition(condition, sensorStates.get(condition.getSensor().getId())));
    }

    private boolean checkCondition(Condition condition, SensorStateAvro sensorState) {
        Integer currentValue = switch (condition.getType()) {
            case ScenarioConditionType.MOTION -> ((MotionSensorAvro) sensorState.getData()).getMotion() ? 1 : 0;
            case ScenarioConditionType.LUMINOSITY -> ((LightSensorAvro) sensorState.getData()).getLuminosity();
            case ScenarioConditionType.SWITCH -> ((SwitchSensorAvro) sensorState.getData()).getState() ? 1 : 0;
            case ScenarioConditionType.TEMPERATURE -> sensorState.getData().getClass().equals(ClimateSensorAvro.class) ? ((ClimateSensorAvro) sensorState.getData()).getTemperatureC() : ((TemperatureSensorAvro) sensorState.getData()).getTemperatureC();
            case ScenarioConditionType.CO2LEVEL -> ((ClimateSensorAvro) sensorState.getData()).getCo2Level();
            case ScenarioConditionType.HUMIDITY -> ((ClimateSensorAvro) sensorState.getData()).getHumidity();
        };

        return switch (condition.getOperation()) {
            case ScenarioConditionOperation.EQUALS -> currentValue.equals(condition.getValue());
            case ScenarioConditionOperation.GREATER_THAN -> currentValue.compareTo(condition.getValue()) > 0;
            case ScenarioConditionOperation.LOWER_THAN -> currentValue.compareTo(condition.getValue()) < 0;
        };
    }

    public void handleActions(Scenario scenario) {
        List<Action> actions = scenario.getActions();

        for (Action action : actions) {

            DeviceActionProto actionProto = actionMapper.mapToProto(action);

            Timestamp timestamp = Timestamp.newBuilder()
                    .setSeconds(Instant.now().getEpochSecond())
                    .setNanos(Instant.now().getNano())
                    .build();

            DeviceActionRequest request = DeviceActionRequest.newBuilder()
                    .setHubId(scenario.getHubId())
                    .setScenarioName(scenario.getName())
                    .setAction(actionProto)
                    .setTimestamp(timestamp)
                    .build();
            log.info("Отправляю deviceActionRequest {}", request);
            hubRouterClient.handleDeviceAction(request);
        }
    }
}
