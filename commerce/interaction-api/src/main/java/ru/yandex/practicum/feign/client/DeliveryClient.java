package ru.yandex.practicum.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

import java.util.UUID;

@FeignClient(name="delivery", path="/api/v1/delivery")
public interface DeliveryClient {

    @PutMapping
    DeliveryDto addDelivery(@RequestBody DeliveryDto deliveryDto);

    @PostMapping("/successful")
    void sendSuccessStateForDelivery(@RequestBody UUID orderId);

    @PostMapping("/picked")
    void sendPickedStateForDelivery(@RequestBody UUID orderId);

    @PostMapping("/failed")
    void sendFailStateForDelivery(@RequestBody UUID orderId);

    @PostMapping("/cost")
    Double calculateDeliveryCost(@RequestBody OrderDto orderDto);
}
