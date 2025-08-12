package ru.yandex.practicum.model;

import jakarta.persistence.*;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "shopping_carts")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(of = { "id" })
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "active_flg")
    private Boolean isActive;

    @ElementCollection
    @CollectionTable(
            name = "shopping_cart_x_products",
            joinColumns = @JoinColumn(name = "cart_id")
    )
    @MapKeyColumn(name = "productId")
    @Column(name = "quantity")
    private Map<UUID, Long> products;
}
