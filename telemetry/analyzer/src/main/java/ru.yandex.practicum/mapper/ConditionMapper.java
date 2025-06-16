package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.model.Condition;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConditionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scenarios", ignore = true)
    @Mapping(target = "sensor.id", source = "sensorId")
    @Mapping(target = "value", expression = "java(extractValue(condition.getValue()))")
    Condition map(ScenarioConditionAvro condition);

    List<Condition> map(List<ScenarioConditionAvro> conditions);

    default Integer extractValue(Object value) {
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Boolean) return (Boolean) value ? 1 : 0;
        throw new IllegalArgumentException("Unsupported type: " + value.getClass());
    }
}
