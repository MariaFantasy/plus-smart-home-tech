package ru.yandex.practicum.feign.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import ru.yandex.practicum.dto.PageableDto;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;

import java.util.UUID;

@FeignClient(name="shopping-store", path="/api/v1/shopping-store")
public interface ShoppingStoreClient {

    @GetMapping
    Page<ProductDto> getByCategory(
            @RequestParam ProductCategory category,
            @RequestParam PageableDto pageableDto
            );

    @PutMapping
    ProductDto addProduct(@RequestBody @Valid ProductDto productDto);

    @PostMapping
    ProductDto update(@RequestBody @Valid ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    Boolean removeProduct(@RequestBody UUID productId);

    @PostMapping("/quantityState")
    Boolean setProductQuantityState(@RequestBody @Valid SetProductQuantityStateRequest request);

    @GetMapping("/{productId}")
    ProductDto getById(@PathVariable UUID productId);
}
