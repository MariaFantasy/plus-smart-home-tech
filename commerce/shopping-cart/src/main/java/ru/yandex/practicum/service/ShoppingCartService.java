package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto get(String username);

    ShoppingCartDto addProduct(String username, Map<UUID, Long> products);

    void delete(String username);

    ShoppingCartDto deleteProduct(String username, List<UUID> products);

    ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request);

    String getUsername(UUID shoppingCartId);
}