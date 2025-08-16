package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.dto.DeliveryDto;

@Mapper(componentModel = "spring", uses = { AddressDtoMapper.class })
public interface DeliveryDtoMapper {
    @Mapping(target = "deliveryState", source = "state")
    DeliveryDto mapToDto(Delivery delivery);

    @Mapping(target = "state", source = "deliveryState")
    Delivery mapFromDto(DeliveryDto deliveryDto);
}
