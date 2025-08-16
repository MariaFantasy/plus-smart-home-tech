package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.*;

import java.util.Map;
import java.util.UUID;

public interface ProductService {
    void create(NewProductInWarehouseRequest request);

    void add(AddProductToWarehouseRequest request);

    void reserve(UUID productId, Long quantity);

    BookedProductsDto checkProductsAvailability(Map<UUID, Long> products);
}
