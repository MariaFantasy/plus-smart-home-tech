package ru.yandex.practicum.dto;

import java.util.ArrayList;
import java.util.List;

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class PageableDto {
    private Integer page = 0;

    private Integer size = 1;

    private List<String> sort = new ArrayList<>();
}
