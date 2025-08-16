package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.feign.client.DeliveryClient;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController implements DeliveryClient {
    private final DeliveryService deliveryService;

    @Override
    public DeliveryDto addDelivery(DeliveryDto deliveryDto) {
        log.info("Пришел PUT запрос с телом {}", deliveryDto);
        final DeliveryDto createdDeliveryDto = deliveryService.create(deliveryDto);
        log.info("Отправлен ответ на PUT запрос с телом {}", createdDeliveryDto);
        return createdDeliveryDto;
    }

    @Override
    public Double calculateDeliveryCost(OrderDto orderDto) {
        log.info("Пришел POST запрос /cost с телом {}", orderDto);
        final Double cost = deliveryService.calculateDeliveryCost(orderDto);
        log.info("Отправлен ответ на POST запрос /cost с deliveryCost={}", cost);
        return cost;
    }

    @Override
    public void sendPickedStateForDelivery(UUID orderId) {
        log.info("Пришел POST запрос /picked для orderId {}", orderId);
        deliveryService.picked(orderId);
        log.info("Отработан POST запрос /picked для orderId {}", orderId);
    }

    @Override
    public void sendSuccessStateForDelivery(UUID orderId) {
        log.info("Пришел POST запрос /successful для orderId {}", orderId);
        deliveryService.setStatusSuccess(orderId);
        log.info("Отработан POST запрос /successful для orderId {}", orderId);
    }

    @Override
    public void sendFailStateForDelivery(UUID orderId) {
        log.info("Пришел POST запрос /failed для orderId {}", orderId);
        deliveryService.setStatusFailed(orderId);
        log.info("Отработан POST запрос /failed для orderId {}", orderId);
    }
}
