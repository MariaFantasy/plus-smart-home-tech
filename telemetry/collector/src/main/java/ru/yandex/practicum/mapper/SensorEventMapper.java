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
        SpecificRecordBase payload;
        if (event instanceof ClimateSensorEvent) {
            payload = mapToAvro((ClimateSensorEvent) event);
        } else if (event instanceof LightSensorEvent) {
            payload = mapToAvro((LightSensorEvent) event);
        } else if (event instanceof MotionSensorEvent) {
            payload = mapToAvro((MotionSensorEvent) event);
        } else if (event instanceof SwitchSensorEvent) {
            payload = mapToAvro((SwitchSensorEvent) event);
        } else if (event instanceof TemperatureSensorEvent) {
            payload = mapToAvro((TemperatureSensorEvent) event);
        } else {
            throw new IllegalArgumentException("Unknown SensorEvent subclass: " + event.getClass());
        }

        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
