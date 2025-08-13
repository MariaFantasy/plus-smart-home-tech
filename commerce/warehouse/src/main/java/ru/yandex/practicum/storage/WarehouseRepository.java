package ru.yandex.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Product;

import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<Product, UUID> {
}
