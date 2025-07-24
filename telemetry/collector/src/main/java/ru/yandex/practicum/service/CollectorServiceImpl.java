package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.KafkaProperties;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.mapper.HubEventMapper;
import ru.yandex.practicum.mapper.SensorEventMapper;

@Slf4j
@Service("collectServiceImpl")
@RequiredArgsConstructor
public class CollectorServiceImpl implements CollectorService {
    private final CollectorKafkaProducer kafkaProducer;
    private final SensorEventMapper sensorEventMapper;
    private final HubEventMapper hubEventMapper;
    private final KafkaProperties kafkaProperties;

    public void loadSensorEvent(SensorEvent sensorEvent) {
        log.info("Start sensor event save {}", sensorEvent);
        kafkaProducer.send(sensorEventMapper.mapToAvro(sensorEvent), kafkaProperties.getTopic().getSensor());
        log.info("Success sensor event save {}", sensorEvent);
    }

    public void loadSensorEvent(SensorEventProto sensorEvent) {
        log.info("Start sensor event save {}", sensorEvent);
        kafkaProducer.send(sensorEventMapper.mapToAvro(sensorEvent), kafkaProperties.getTopic().getSensor());
        log.info("Success sensor event save {}", sensorEvent);
    }

    public void loadHubEvent(HubEvent hubEvent) {
        log.info("Start hub event save {}", hubEvent);
        kafkaProducer.send(hubEventMapper.mapToAvro(hubEvent), kafkaProperties.getTopic().getHub());
        log.info("Success hub event save {}", hubEvent);
    }

    public void loadHubEvent(HubEventProto hubEvent) {
        log.info("Start hub event save {}", hubEvent);
        kafkaProducer.send(hubEventMapper.mapToAvro(hubEvent), kafkaProperties.getTopic().getHub());
        log.info("Success hub event save {}", hubEvent);
    }
}
