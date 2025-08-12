package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;

@lombok.Data
@lombok.AllArgsConstructor
public class BookedProductsDto {
    @NotNull
    private Double deliveryWeight;

    @NotNull
    private Double deliveryVolume;

    @NotNull
    private Boolean fragile;
}
