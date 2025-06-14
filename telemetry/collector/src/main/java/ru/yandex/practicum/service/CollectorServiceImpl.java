package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.mapper.HubEventMapper;
import ru.yandex.practicum.mapper.SensorEventMapper;

@Service("collectServiceImpl")
@RequiredArgsConstructor
public class CollectorServiceImpl implements CollectorService {
    private final CollectorKafkaProducer kafkaProducer;
    private final SensorEventMapper sensorEventMapper;
    private final HubEventMapper hubEventMapper;

    private static final String SENSOR_KAFKA_TOPIC = "telemetry.sensors.v1";
    private static final String HUB_KAFKA_TOPIC = "telemetry.hubs.v1";

    public void loadSensorEvent(SensorEvent sensorEvent) {
        kafkaProducer.send(sensorEventMapper.mapToAvro(sensorEvent), SENSOR_KAFKA_TOPIC);
    }

    public void loadHubEvent(HubEvent hubEvent) {
        kafkaProducer.send(hubEventMapper.mapToAvro(hubEvent), HUB_KAFKA_TOPIC);
    }
}
