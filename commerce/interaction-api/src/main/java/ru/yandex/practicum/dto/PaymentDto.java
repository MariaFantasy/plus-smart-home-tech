package ru.yandex.practicum.dto;

import java.util.UUID;

@lombok.Data
@lombok.AllArgsConstructor
@lombok.RequiredArgsConstructor
public class PaymentDto {
    private UUID paymentId;

    private Double totalPayment;

    private Double deliveryTotal;

    private Double feeTotal;
}
