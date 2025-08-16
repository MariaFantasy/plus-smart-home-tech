package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;
import ru.yandex.practicum.feign.client.OrderClient;
import ru.yandex.practicum.service.OrderService;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/order")
@RequiredArgsConstructor
public class OrderController implements OrderClient {
    private final OrderService orderService;

    @Override
    public Collection<OrderDto> getOrders(String username) {
        log.info("Пришел GET запрос для username={}", username);
        final Collection<OrderDto> orderDtos = orderService.getByUsername(username);
        log.info("Отправлен ответ GET запрос с телом {}", orderDtos);
        return orderDtos;
    }

    @Override
    public OrderDto addOrder(CreateNewOrderRequest request) {
        log.info("Пришел PUT запрос с телом {}", request);
        final OrderDto orderDto = orderService.create(request);
        log.info("Отправлен ответ PUT запрос с телом {}", orderDto);
        return orderDto;
    }

    @Override
    public OrderDto calculateTotalPrice(UUID orderId) {
        log.info("Пришел POST запрос /calculate/total для заказа {}", orderId);
        final OrderDto orderDto = orderService.calculateTotalPrice(orderId);
        log.info("Отправлен ответ POST запрос /calculate/total с телом {}", orderDto);
        return orderDto;
    }

    @Override
    public OrderDto calculateDeliveryPrice(UUID orderId) {
        log.info("Пришел POST запрос /calculate/delivery для заказа {}", orderId);
        final OrderDto orderDto = orderService.calculateDeliveryPrice(orderId);
        log.info("Отправлен ответ POST запрос /calculate/delivery с телом {}", orderDto);
        return orderDto;
    }

    @Override
    public OrderDto payOrder(UUID orderId) {
        log.info("Пришел POST запрос /payment для заказа {}", orderId);
        final OrderDto orderDto = orderService.pay(orderId);
        log.info("Отправлен ответ POST запрос /payment с телом {}", orderDto);
        return orderDto;
    }

    @Override
    public OrderDto sendFailedPayment(UUID orderId) {
        log.info("Пришел POST запрос /payment/failed для заказа {}", orderId);
        final OrderDto orderDto = orderService.sendFailedPayment(orderId);
        log.info("Отправлен ответ POST запрос /payment/failed с телом {}", orderDto);
        return orderDto;
    }

    @Override
    public OrderDto assemblyOrder(UUID orderId) {
        log.info("Пришел POST запрос /assembly для заказа {}", orderId);
        final OrderDto orderDto = orderService.assembly(orderId);
        log.info("Отправлен ответ POST запрос /assembly с телом {}", orderDto);
        return orderDto;
    }

    @Override
    public OrderDto sendFailedAssembly(UUID orderId) {
        log.info("Пришел POST запрос /assembly/failed для заказа {}", orderId);
        final OrderDto orderDto = orderService.sendFailedAssembly(orderId);
        log.info("Отправлен ответ POST запрос /assembly/failed с телом {}", orderDto);
        return orderDto;
    }

    @Override
    public OrderDto sendOrderToDelivery(UUID orderId) {
        log.info("Пришел POST запрос /delivery для заказа {}", orderId);
        final OrderDto orderDto = orderService.sendToDelivery(orderId);
        log.info("Отправлен ответ POST запрос /delivery с телом {}", orderDto);
        return orderDto;
    }

    @Override
    public OrderDto sendFailedDelivery(UUID orderId) {
        log.info("Пришел POST запрос /delivery/failed для заказа {}", orderId);
        final OrderDto orderDto = orderService.sendFailedDelivery(orderId);
        log.info("Отправлен ответ POST запрос /delivery/failed с телом {}", orderDto);
        return orderDto;
    }

    @Override
    public OrderDto completeOrder(UUID orderId) {
        log.info("Пришел POST запрос /completed для заказа {}", orderId);
        final OrderDto orderDto = orderService.completeOrder(orderId);
        log.info("Отправлен ответ POST запрос /completed с телом {}", orderDto);
        return orderDto;
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {
        log.info("Пришел POST запрос /return с телом {}", request);
        final OrderDto orderDto = orderService.returnOrder(request);
        log.info("Отправлен ответ POST запрос /return с телом {}", orderDto);
        return orderDto;
    }
}
