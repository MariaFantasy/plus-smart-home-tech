package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.ArrayList;
import java.util.List;

@lombok.Data
@lombok.AllArgsConstructor
public class PageableDto {
    @PositiveOrZero
    private Integer page = 0;

    @Positive
    private Integer size = 1;

    private List<String> sort = new ArrayList<>();
}
