package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.ShippedToDeliveryRequest;

public interface OrderBookingService {

    void create(AssemblyProductsForOrderRequest request);

    void sendToDelivery(ShippedToDeliveryRequest request);
}
