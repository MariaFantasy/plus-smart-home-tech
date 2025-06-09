package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Mapper(componentModel = "spring")
public interface SensorEventMapper {
    SensorEventAvro mapToAvro(SensorEvent event);
}
