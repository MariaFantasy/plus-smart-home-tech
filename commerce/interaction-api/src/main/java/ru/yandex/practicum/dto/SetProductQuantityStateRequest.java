package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@lombok.Data
@lombok.AllArgsConstructor
public class SetProductQuantityStateRequest {
    @NotNull(message = "The productId field cannot be empty.")
    private UUID productId;

    @NotNull(message = "The quantityState field cannot be empty.")
    private QuantityState quantityState;
}
