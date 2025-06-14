package ru.yandex.practicum.dto.hub.device;

import lombok.Data;

@Data
public class DeviceAction {
    private String sensorId;
    private DeviceActionType type;
    private int value;
}
