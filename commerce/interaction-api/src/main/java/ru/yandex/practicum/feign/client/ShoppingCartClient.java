package ru.yandex.practicum.feign.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name="shopping-cart", path="/api/v1/shopping-cart")
public interface ShoppingCartClient {

    @GetMapping
    ShoppingCartDto get(@RequestParam String username);

    @PutMapping
    ShoppingCartDto addProduct(@RequestParam String username, @RequestBody Map<UUID, Long> products);

    @DeleteMapping
    void delete(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto deleteProduct(@RequestParam String username, @RequestBody List<UUID> products);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantity(@RequestParam String username, @RequestBody @Valid ChangeProductQuantityRequest request);

    @GetMapping("/username")
    String getUsername(@RequestParam UUID shoppingCartId);
}
