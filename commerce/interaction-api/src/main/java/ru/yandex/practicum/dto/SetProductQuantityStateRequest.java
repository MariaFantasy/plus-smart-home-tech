package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@lombok.Data
@lombok.AllArgsConstructor
public class SetProductQuantityStateRequest {
    @NotNull
    private UUID productId;

    @NotNull
    private QuantityState quantityState;
}
