package ru.yandex.practicum.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

import java.util.UUID;

@FeignClient(name="payment", path="/api/v1/payment")
public interface PaymentClient {

    @PostMapping
    PaymentDto addPayment(@RequestBody OrderDto orderDto);

    @PostMapping("/totalCost")
    Double calculateTotalCost(@RequestBody OrderDto orderDto);

    @PostMapping("/refund")
    void sendSuccessStateForPayment(@RequestBody UUID orderId);

    @PostMapping("/productCost")
    Double calculateProductCost(@RequestBody OrderDto orderDto);

    @PostMapping("/failed")
    void sendFailStateForPayment(@RequestBody UUID orderId);
}
