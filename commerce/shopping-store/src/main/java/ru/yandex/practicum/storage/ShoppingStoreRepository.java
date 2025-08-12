package ru.yandex.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.model.Product;

import java.util.List;
import java.util.UUID;

public interface ShoppingStoreRepository extends JpaRepository<Product, UUID> {

    @Query("""
        SELECT p
        FROM Product AS p
        WHERE product_category = ?1
    """)
    List<Product> findByCategory(ProductCategory category, Pageable pageable);
}
