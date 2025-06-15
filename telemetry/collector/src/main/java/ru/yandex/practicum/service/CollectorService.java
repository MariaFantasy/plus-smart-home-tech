package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface CollectorService {
    public void loadSensorEvent(SensorEvent sensorEvent);

    public void loadSensorEvent(SensorEventProto sensorEvent);

    public void loadHubEvent(HubEvent hubEvent);

    public void loadHubEvent(HubEventProto hubEvent);
}
