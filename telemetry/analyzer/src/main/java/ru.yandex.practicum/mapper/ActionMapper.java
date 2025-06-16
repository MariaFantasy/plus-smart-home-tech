package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.model.Action;

@Mapper(componentModel = "spring")
public interface ActionMapper {
    DeviceActionProto mapToProto(Action action);
    Action mapFromAvro(DeviceActionAvro avro);
}
