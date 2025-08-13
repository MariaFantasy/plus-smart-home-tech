package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.model.Product;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {
    @Mapping(target = "id", source = "productId")
    Product mapFromDto(ProductDto productDto);

    @Mapping(target = "productId", source = "id")
    ProductDto mapToDto(Product product);
}
