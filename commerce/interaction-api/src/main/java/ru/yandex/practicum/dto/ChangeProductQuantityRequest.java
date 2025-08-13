package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@lombok.Data
@lombok.AllArgsConstructor
public class ChangeProductQuantityRequest {
    @NotNull
    private UUID productId;

    @NotNull
    private Long newQuantity;
}
