package ru.yandex.practicum.model;

import jakarta.persistence.*;
import ru.yandex.practicum.dto.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "deliveries")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(of = { "deliveryId" })
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private UUID deliveryId;

    @ManyToOne
    @JoinColumn(name = "from_address_id")
    private Address fromAddress;

    @ManyToOne
    @JoinColumn(name = "to_address_id")
    private Address toAddress;

    @Column(name = "order_id")
    private Boolean orderId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private DeliveryState state;
}
