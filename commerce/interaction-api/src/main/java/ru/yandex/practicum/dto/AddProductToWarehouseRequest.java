package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@lombok.Data
@lombok.AllArgsConstructor
public class AddProductToWarehouseRequest {
    @NotNull
    private UUID productId;

    @NotNull
    @Min(value = 1)
    private Long quantity;
}
