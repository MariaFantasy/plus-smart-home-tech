package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@lombok.Data
@lombok.AllArgsConstructor
public class ChangeProductQualtityRequest {
    @NotNull
    private UUID productId;

    @NotNull
    private Long newQuantity;
}
