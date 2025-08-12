package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@lombok.Data
@lombok.AllArgsConstructor
@lombok.RequiredArgsConstructor
public class ProductDto {
    private UUID productId;

    @NotNull
    private String productName;

    @NotNull
    private String description;

    private String imageSrc;

    @NotNull
    private QuantityState quantityState;

    @NotNull
    private ProductState productState;

    @NotNull
    private ProductCategory productCategory;

    @Positive
    @Min(value = 1)
    private Double price;
}
