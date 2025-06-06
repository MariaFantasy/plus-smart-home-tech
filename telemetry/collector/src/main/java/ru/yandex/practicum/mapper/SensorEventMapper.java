package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Mapper(componentModel = "spring")
public class SensorEventMapper {
    SensorEventAvro mapToAvro(SensorEvent event);
}
