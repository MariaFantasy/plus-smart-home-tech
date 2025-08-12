package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;

import java.util.Collection;
import java.util.UUID;

public interface ShoppingStoreService {
    Collection<ProductDto> getByCategory(ProductCategory category, Pageable pageable);

    ProductDto addProduct(ProductDto productDto);

    ProductDto update(ProductDto productDto);

    Boolean removeProduct(UUID productId);

    Boolean setProductQuantityState(SetProductQuantityStateRequest request);

    ProductDto getById(UUID productId);
}
