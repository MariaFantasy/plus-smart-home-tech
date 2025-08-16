package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

import java.util.UUID;

public interface DeliveryService {
    DeliveryDto create(DeliveryDto deliveryDto);

    Double calculateDeliveryCost(OrderDto orderDto);

    void picked(UUID orderId);

    void setStatusCancelled(UUID orderId);

    void setStatusSuccess(UUID orderId);

    void setStatusFailed(UUID orderId);
}
