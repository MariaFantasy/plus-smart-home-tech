package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

import java.util.UUID;

public interface PaymentService {
    PaymentDto create(OrderDto orderDto);

    Double calculateProductCost(OrderDto orderDto);

    Double calculateTotalCost(OrderDto orderDto);

    void sendSuccessState(UUID orderId);

    void sendFailedState(UUID orderId);
}
