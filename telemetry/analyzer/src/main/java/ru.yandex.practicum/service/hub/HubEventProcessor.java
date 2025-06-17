package ru.yandex.practicum.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.mapper.ScenarioMapper;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.repository.SensorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {
    private final KafkaProperties kafkaProperties;
    private final HubEventKafkaConsumer consumer;
    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;
    private final ScenarioMapper scenarioMapper;

    @Override
    public void run() {
        try {
            consumer.subscribe(kafkaProperties.getHub().getTopic());

            while (true) {
                consumer.read(this::handleRecord);
            }

        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
            log.info("Чтение топика {} остановлено.", kafkaProperties.getHub().getTopic());
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

    public void handleRecord(HubEventAvro avro) {
        SpecificRecordBase payload = (SpecificRecordBase) avro.getPayload();
        if (payload.getClass().equals(DeviceAddedEventAvro.class)) {
            log.info("DeviceAddedEvent");
            DeviceAddedEventAvro deviceAvro = (DeviceAddedEventAvro) payload;
            Sensor sensor = new Sensor();
            sensor.setId(deviceAvro.getId());
            sensor.setHubId(avro.getHubId());
            log.info("Sensor: {}", sensor);
            sensorRepository.save(sensor);
        } else if (payload.getClass().equals(DeviceRemovedEventAvro.class)) {
            log.info("DeviceRemovedEvent");
            sensorRepository.deleteById(((DeviceRemovedEventAvro) avro.getPayload()).getId());
        } else if (payload.getClass().equals(ScenarioAddedEventAvro.class)) {
            log.info("ScenarioAddedEvent");
            log.info("ScenarioAddedEventAvro: {}", avro);
            ScenarioAddedEventAvro scenarioAvro = (ScenarioAddedEventAvro) avro.getPayload();
            Scenario scenario = scenarioMapper.mapFromAvro(scenarioAvro, avro.getHubId());
            log.info("Scenario: {}", scenario);
            scenarioRepository.save(scenario);
        } else if (payload.getClass().equals(ScenarioRemovedEventAvro.class)) {
            log.info("ScenarioRemovedEvent");
            scenarioRepository.deleteByName(((ScenarioRemovedEventAvro) avro.getPayload()).getName());
        }
    }
}
