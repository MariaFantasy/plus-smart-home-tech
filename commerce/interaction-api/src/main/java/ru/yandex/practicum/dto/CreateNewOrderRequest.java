package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;

@lombok.Data
@lombok.AllArgsConstructor
public class CreateNewOrderRequest {
    @NotNull
    private ShoppingCartDto shoppingCart;

    @NotNull
    private AddressDto deliveryAddress;
}
