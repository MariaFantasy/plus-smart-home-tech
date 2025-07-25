package ru.yandex.practicum.mapper;

import org.apache.avro.specific.SpecificRecordBase;
import org.mapstruct.*;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.dto.hub.device.*;
import ru.yandex.practicum.dto.hub.scenario.*;

import java.time.Instant;
import java.util.List;

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

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
    DeviceTypeAvro mapToAvro(DeviceTypeProto proto);
    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
    @Mapping(target = "value", expression = "java(proto.getValueCase() == ScenarioConditionProto.ValueCase.BOOL_VALUE ? proto.getBoolValue() : proto.getIntValue())")
    ScenarioConditionAvro mapToAvro(ScenarioConditionProto proto);
    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
    DeviceActionAvro mapToAvro(DeviceActionProto proto);

    @Named("mapConditions")
    List<ScenarioConditionAvro> mapConditions(List<ScenarioConditionProto> protoList);
    @Named("mapActions")
    List<DeviceActionAvro> mapActions(List<DeviceActionProto> protoList);

    DeviceAddedEventAvro mapToAvro(DeviceAddedEventProto event);
    DeviceRemovedEventAvro mapToAvro(DeviceRemovedEventProto event);
    @Mapping(source = "conditionsList", target = "conditions", qualifiedByName = "mapConditions")
    @Mapping(source = "actionsList", target = "actions", qualifiedByName = "mapActions")
    ScenarioAddedEventAvro mapToAvro(ScenarioAddedEventProto event);
    ScenarioRemovedEventAvro mapToAvro(ScenarioRemovedEventProto event);

    default SpecificRecordBase mapToAvro(HubEventProto event) {
        SpecificRecordBase payload;
        if (event.hasDeviceAdded()) {
            payload = mapToAvro(event.getDeviceAdded());
        } else if (event.hasDeviceRemoved()) {
            payload = mapToAvro(event.getDeviceRemoved());
        } else if (event.hasScenarioAdded()) {
            payload = mapToAvro(event.getScenarioAdded());
        } else if (event.hasScenarioRemoved()) {
            payload = mapToAvro(event.getScenarioRemoved());
        } else {
            throw new IllegalArgumentException("Unknown HubEvent subclass: " + event.getClass());
        }

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }
}
