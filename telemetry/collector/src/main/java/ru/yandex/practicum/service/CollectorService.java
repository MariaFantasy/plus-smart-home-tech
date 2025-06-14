package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;

public interface CollectorService {
    public void loadSensorEvent(SensorEvent sensorEvent);

    public void loadHubEvent(HubEvent hubEvent);
}
