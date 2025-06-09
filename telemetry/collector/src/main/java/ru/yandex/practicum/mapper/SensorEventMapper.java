package ru.yandex.practicum.mapper;

import org.apache.avro.specific.SpecificRecordBase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.dto.sensor.*;

@Mapper(componentModel = "spring")
public interface SensorEventMapper {
    @Mapping(target = ".", source = ".", qualifiedByName = "mapClasses")
    @Mapping(target = "id", source = "id", qualifiedByName = "mapClasses")
    SensorEventAvro mapToAvro(SensorEvent event);

    @SubclassMapping(target = ClimateSensorAvro.class, source = ClimateSensorEvent.class)
    @SubclassMapping(target = LightSensorAvro.class, source = LightSensorEvent.class)
    @SubclassMapping(target = MotionSensorAvro.class, source = MotionSensorEvent.class)
    @SubclassMapping(target = SwitchSensorAvro.class, source = SwitchSensorEvent.class)
    @SubclassMapping(target = TemperatureSensorAvro.class, source = TemperatureSensorEvent.class)
    Object mapClasses(SensorEvent event);

    ClimateSensorAvro mapToAvro(ClimateSensorEvent event);

    LightSensorAvro mapToAvro(LightSensorEvent event);

    MotionSensorAvro mapToAvro(MotionSensorEvent event);

    SwitchSensorAvro mapToAvro(SwitchSensorEvent event);

    TemperatureSensorAvro mapToAvro(TemperatureSensorEvent event);
}
