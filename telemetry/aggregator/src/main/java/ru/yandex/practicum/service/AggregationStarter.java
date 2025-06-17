package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Класс AggregationStarter, ответственный за запуск агрегации данных.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private final KafkaProperties kafkaProperties;
    private final AggregatorKafkaProducer producer;
    private final AggregatorKafkaConsumer consumer;
    private static final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    /**
     * Метод для начала процесса агрегации данных.
     * Подписывается на топики для получения событий от датчиков,
     * формирует снимок их состояния и записывает в кафку.
     */
    public void start() {
        try {
            log.info("Подписка на топик: {}", kafkaProperties.getTopic().getSensor());
            consumer.subscribe(kafkaProperties.getTopic().getSensor());
            log.info("Успешная подписка на топик: {}", kafkaProperties.getTopic().getSensor());

            while (true) {
                consumer.read(this::handleRecord);
            }

        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
            log.info("Чтение топика {} остановлено.", kafkaProperties.getTopic().getSensor());
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                producer.flush();
                consumer.commit();

            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        log.info("Получен event {}", event);
        SensorsSnapshotAvro snapshot;
        if (snapshots.containsKey(event.getHubId())) {
            snapshot = snapshots.get(event.getHubId());
        } else {
            snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(event.getHubId())
                    .setTimestamp(event.getTimestamp())
                    .setSensorsState(new HashMap<>())
                    .build();
        }

        if (snapshot.getSensorsState().containsKey(event.getId())) {
            SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());
            if (event.getTimestamp().isBefore(oldState.getTimestamp())) {
                return Optional.empty();
            }
            if (event.getPayload().equals(oldState.getData())) {
                return Optional.empty();
            }
        }

        SensorStateAvro sensorStateAvro = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();
        snapshot.getSensorsState().put(event.getId(), sensorStateAvro);
        snapshot.setTimestamp(event.getTimestamp());
        snapshots.put(event.getHubId(), snapshot);
        log.info("Отправлен snapshot {}", snapshot);
        return Optional.of(snapshot);
    }

    public void handleRecord(SensorEventAvro avro) {
        Optional<SensorsSnapshotAvro> snapshotAvro = updateState(avro);
        snapshotAvro.ifPresent(sensorsSnapshotAvro -> producer.send(sensorsSnapshotAvro, kafkaProperties.getTopic().getSnapshot()));
    }
}