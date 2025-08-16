package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void createNewProduct(NewProductInWarehouseRequest request);

    void sendOrderForDelivery(ShippedToDeliveryRequest request);

    void addReturnedProducts(Map<UUID, Long> products);

    BookedProductsDto checkProductsAvailability(ShoppingCartDto shoppingCartDto);

    BookedProductsDto assemblyOrderToDelivery(AssemblyProductsForOrderRequest request);

    void addProducts(AddProductToWarehouseRequest request);

    AddressDto getAddress();
}
