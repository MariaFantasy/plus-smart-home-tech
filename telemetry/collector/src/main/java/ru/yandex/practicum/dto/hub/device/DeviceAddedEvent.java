package ru.yandex.practicum.dto.hub.device;

import lombok.Data;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.hub.HubEventType;

@Data
public class DeviceAddedEvent extends HubEvent {
    private String id;
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
