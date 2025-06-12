package ru.yandex.practicum.mapper;

import org.apache.avro.specific.SpecificRecordBase;
import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.dto.hub.device.*;
import ru.yandex.practicum.dto.hub.scenario.*;

@Mapper(componentModel = "spring")
public interface HubEventMapper {
    DeviceAddedEventAvro mapToAvro(DeviceAddedEvent event);
    DeviceRemovedEventAvro mapToAvro(DeviceRemovedEvent event);
    ScenarioAddedEventAvro mapToAvro(ScenarioAddedEvent event);
    ScenarioRemovedEventAvro mapToAvro(ScenarioRemovedEvent event);

    default SpecificRecordBase mapToAvro(HubEvent event) {
        SpecificRecordBase payload;
        if (event instanceof DeviceAddedEvent) {
            payload = mapToAvro((DeviceAddedEvent) event);
        } else if (event instanceof DeviceRemovedEvent) {
            payload = mapToAvro((DeviceRemovedEvent) event);
        } else if (event instanceof ScenarioAddedEvent) {
            payload = mapToAvro((ScenarioAddedEvent) event);
        } else if (event instanceof ScenarioRemovedEvent) {
            payload = mapToAvro((ScenarioRemovedEvent) event);
        } else {
            throw new IllegalArgumentException("Unknown HubEvent subclass: " + event.getClass());
        }

        return SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
