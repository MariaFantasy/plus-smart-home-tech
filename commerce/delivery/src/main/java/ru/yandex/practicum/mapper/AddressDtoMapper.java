package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.model.Address;

@Mapper(componentModel = "spring")
public interface AddressDtoMapper {
    AddressDto mapToDto(Address address);

    @Mapping(target = "addressId", ignore = true)
    Address mapFromDto(AddressDto addressDto);
}
