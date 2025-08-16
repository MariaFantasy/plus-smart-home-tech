package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;

import java.util.Collection;
import java.util.UUID;

public interface OrderService {
    Collection<OrderDto> getByUsername(String username);

    OrderDto create(CreateNewOrderRequest request);

    OrderDto calculateTotalPrice(UUID orderId);

    OrderDto calculateDeliveryPrice(UUID orderId);

    OrderDto pay(UUID orderId);

    OrderDto sendFailedPayment(UUID orderId);

    OrderDto assembly(UUID orderId);

    OrderDto sendFailedAssembly(UUID orderId);

    OrderDto sendToDelivery(UUID orderId);

    OrderDto sendFailedDelivery(UUID orderId);

    OrderDto completeOrder(UUID orderId);

    OrderDto returnOrder(ProductReturnRequest request);
}
