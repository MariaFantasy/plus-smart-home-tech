package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

@lombok.Data
@lombok.AllArgsConstructor
public class ProductReturnRequest {
    @NotNull
    private UUID orderId;

    @NotNull
    private Map<UUID, Long> products;
}
