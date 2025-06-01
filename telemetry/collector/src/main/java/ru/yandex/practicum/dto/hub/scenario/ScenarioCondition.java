package ru.yandex.practicum.dto.hub.scenario;

import lombok.Data;

@Data
public class ScenarioCondition {
    private String sensorId;
    private ScenarioConditionType type;
    private ScenarioConditionOperation operation;
    private int value;
}
