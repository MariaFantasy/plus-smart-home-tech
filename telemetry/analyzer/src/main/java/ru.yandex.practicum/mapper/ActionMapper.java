package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.model.Action;

@Mapper(componentModel = "spring")
public interface ActionMapper {
    @Mapping(target = "sensorId", source = "sensor.id")
    DeviceActionProto mapToProto(Action action);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scenarios", ignore = true)
    @Mapping(target = "sensor.id", source = "sensorId")
    Action mapFromAvro(DeviceActionAvro avro);
}
