package ru.yandex.practicum.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;

import java.util.Collection;
import java.util.UUID;

@FeignClient(name="order", path="/api/v1/order")
public interface OrderClient {

    @GetMapping
    Collection<OrderDto> getOrders(@RequestParam String username);

    @PutMapping
    OrderDto addOrder(@RequestBody CreateNewOrderRequest request);

    @PostMapping("/return")
    OrderDto returnOrder(@RequestBody ProductReturnRequest request);

    @PostMapping("/payment")
    OrderDto payOrder(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto sendFailedPayment(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    OrderDto sendOrderToDelivery(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto sendFailedDelivery(@RequestBody UUID orderId);

    @PostMapping("/completed")
    OrderDto completeOrder(@RequestBody UUID orderId);

    @PostMapping("/calculate/total")
    OrderDto calculateTotalPrice(@RequestBody UUID orderId);

    @PostMapping("/calculate/delivery")
    OrderDto calculateDeliveryPrice(@RequestBody UUID orderId);

    @PostMapping("/assembly")
    OrderDto assemblyOrder(@RequestBody UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto sendFailedAssembly(@RequestBody UUID orderId);
}
