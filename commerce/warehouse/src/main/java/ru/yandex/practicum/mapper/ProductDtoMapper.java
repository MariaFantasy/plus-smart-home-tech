package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.model.Product;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {
    @Mapping(target = "id", source = "productId")
    @Mapping(target = "width", source = "productDto.dimensionDto.width")
    @Mapping(target = "height", source = "productDto.dimensionDto.height")
    @Mapping(target = "depth", source = "productDto.dimensionDto.depth")
    @Mapping(target = "quantity", ignore = true)
    Product mapFromDto(NewProductInWarehouseRequest productDto);
}
