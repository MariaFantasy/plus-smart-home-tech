package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@lombok.Data
@lombok.AllArgsConstructor
public class DimensionDto {
    @NotNull
    @Min(value = 1)
    private Double width;

    @NotNull
    @Min(value = 1)
    private Double height;

    @NotNull
    @Min(value = 1)
    private Double depth;
}
