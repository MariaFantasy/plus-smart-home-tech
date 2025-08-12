package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.*;

public interface WarehouseService {
    void putNewProduct(NewProductInWarehouseRequest request);

    BookedProductsDto checkAvailable(ShoppingCartDto shoppingCartDto);

    void addNewProduct(AddProductToWarehouseRequest request);

    AddressDto getAddress();
}
