package ru.yandex.practicum.dto.hub.scenario;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.hub.HubEventType;
import ru.yandex.practicum.dto.hub.device.DeviceAction;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class ScenarioAddedEvent extends HubEvent {
    private String name;
    private List<ScenarioCondition> conditions;
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
