package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.model.Scenario;

@Mapper(componentModel = "spring")
public interface ScenarioMapper {
    Scenario mapFromAvro(ScenarioAddedEventAvro avro);
}
