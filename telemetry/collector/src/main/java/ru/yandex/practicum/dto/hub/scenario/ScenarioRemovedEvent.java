package ru.yandex.practicum.dto.hub.scenario;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.hub.HubEventType;

@Data
@EqualsAndHashCode(callSuper=false)
public class ScenarioRemovedEvent extends HubEvent {
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
