package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;

@Service("collectServiceImpl")
@RequiredArgsConstructor
public class CollectorServiceImpl implements CollectorService {
    private final CollectorKafkaProducer kafkaProducer;

    public void loadSensorEvent(SensorEvent sensorEvent) {
        //
    }

    public void loadHubEvent(HubEvent hubEvent) {
        //
    }
}
