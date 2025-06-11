package ru.yandex.practicum.mapper;

import org.apache.avro.specific.SpecificRecordBase;
import org.mapstruct.*;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.dto.sensor.*;

@Mapper(componentModel = "spring")
public interface SensorEventMapper {
    ClimateSensorAvro mapToAvro(ClimateSensorEvent event);
    LightSensorAvro mapToAvro(LightSensorEvent event);
    MotionSensorAvro mapToAvro(MotionSensorEvent event);
    SwitchSensorAvro mapToAvro(SwitchSensorEvent event);
    TemperatureSensorAvro mapToAvro(TemperatureSensorEvent event);

    default SpecificRecordBase mapToAvro(SensorEvent event) {
        if (event instanceof ClimateSensorEvent) {
            return mapToAvro((ClimateSensorEvent) event);
        } else if (event instanceof LightSensorEvent) {
            return mapToAvro((LightSensorEvent) event);
        } else if (event instanceof MotionSensorEvent) {
            return mapToAvro((MotionSensorEvent) event);
        } else if (event instanceof SwitchSensorEvent) {
            return mapToAvro((SwitchSensorEvent) event);
        } else if (event instanceof TemperatureSensorEvent) {
            return mapToAvro((TemperatureSensorEvent) event);
        } else {
            throw new IllegalArgumentException("Unknown SensorEvent subclass: " + event.getClass());
        }
    }
}
