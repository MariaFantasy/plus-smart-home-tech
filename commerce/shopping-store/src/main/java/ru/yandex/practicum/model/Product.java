package ru.yandex.practicum.model;

import jakarta.persistence.*;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductState;
import ru.yandex.practicum.dto.QuantityState;

import java.util.UUID;

@Entity
@Table(name = "products")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(of = { "id" })
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private UUID id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name = "image_src")
    private String imageSrc;

    @Column(name = "quantity_state")
    @Enumerated(EnumType.STRING)
    private QuantityState quantityState;

    @Column(name = "product_state")
    @Enumerated(EnumType.STRING)
    private ProductState productState;

    @Column(name = "product_category")
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Column(name = "price")
    private Double price;
}
