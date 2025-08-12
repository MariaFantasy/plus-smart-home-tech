package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@lombok.Data
@lombok.AllArgsConstructor
@lombok.RequiredArgsConstructor
public class NewProductInWarehouseRequest {
    @NotNull
    private UUID productId;

    private Boolean fragile;

    @NotNull
    private DimensionDto dimensionDto;

    @NotNull
    @Min(value = 1)
    private Double weight;
}
