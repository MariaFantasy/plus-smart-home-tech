package ru.yandex.practicum.feign.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(name="warehouse", path="/api/v1/warehouse")
public interface WarehouseClient {

    @PutMapping
    void createNewProduct(@RequestBody @Valid NewProductInWarehouseRequest request);

    @PostMapping("/shipped")
    void sendOrderForDelivery(@RequestBody ShippedToDeliveryRequest request);

    @PostMapping("/return")
    void addReturnedProducts(@RequestBody @NotNull Map<UUID, Long> products);

    @PostMapping("/check")
    BookedProductsDto checkProductsAvailability(@RequestBody @Valid ShoppingCartDto shoppingCartDto);

    @PostMapping("/assembly")
    BookedProductsDto assemblyOrderToDelivery(@RequestBody AssemblyProductsForOrderRequest request);

    @PostMapping("/add")
    void addProducts(@RequestBody @Valid AddProductToWarehouseRequest request);

    @GetMapping("/address")
    AddressDto getAddress();
}
