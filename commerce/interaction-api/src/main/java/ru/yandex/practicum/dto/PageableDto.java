package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

@lombok.Data
@lombok.AllArgsConstructor
public class PageableDto {
    @PositiveOrZero
    private Integer page;

    @Positive
    private Integer size;

    @NotNull
    private List<String> sort;
}
