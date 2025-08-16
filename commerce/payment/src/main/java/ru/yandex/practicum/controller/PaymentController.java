package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.feign.client.PaymentClient;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController implements PaymentClient {
    private final PaymentService paymentService;

    @Override
    public PaymentDto addPayment(OrderDto orderDto) {
        log.info("Пришел POST запрос с телом {}", orderDto);
        final PaymentDto paymentDto = paymentService.create(orderDto);
        log.info("Отправлен ответ на POST запрос с телом {}", paymentDto);
        return paymentDto;
    }

    @Override
    public Double calculateProductCost(OrderDto orderDto) {
        log.info("Пришел POST запрос /productCost с телом {}", orderDto);
        final Double productCost = paymentService.calculateProductCost(orderDto);
        log.info("Отправлен ответ на POST запрос /productCost с телом {}", productCost);
        return productCost;
    }

    @Override
    public Double calculateTotalCost(OrderDto orderDto) {
        log.info("Пришел POST запрос /totalCost с телом {}", orderDto);
        final Double totalCost = paymentService.calculateTotalCost(orderDto);
        log.info("Отправлен ответ на POST запрос /totalCost с телом {}", totalCost);
        return totalCost;
    }

    @Override
    public void sendSuccessStateForPayment(UUID orderId) {
        log.info("Пришел POST запрос /refund с телом {}", orderId);
        paymentService.sendSuccessState(orderId);
        log.info("Отработан POST запрос /refund с телом {}", orderId);
    }

    @Override
    public void sendFailStateForPayment(UUID orderId) {
        log.info("Пришел POST запрос /failed с телом {}", orderId);
        paymentService.sendFailedState(orderId);
        log.info("Отработан POST запрос /failed с телом {}", orderId);
    }
}
