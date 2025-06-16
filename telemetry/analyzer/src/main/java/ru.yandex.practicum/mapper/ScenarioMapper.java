package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.model.Scenario;

@Mapper(componentModel = "spring", uses = {ConditionMapper.class, ActionMapper.class})
public interface ScenarioMapper {
    @Mapping(target = "id", ignore = true)
    Scenario mapFromAvro(ScenarioAddedEventAvro avro, String hubId);
}
