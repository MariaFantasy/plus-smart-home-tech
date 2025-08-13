package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Map;
import java.util.UUID;

@lombok.Data
@lombok.AllArgsConstructor
public class ShoppingCartDto {
    @NotNull
    private UUID shoppingCartId;

    @NotNull
    private Map<UUID, @PositiveOrZero Long> products;
}
