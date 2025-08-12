package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.feign.client.ShoppingStoreClient;
import ru.yandex.practicum.service.ShoppingStoreService;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreClient {
    private final ShoppingStoreService shoppingStoreService;

    @Override
    public Collection<ProductDto> getByCategory(ProductCategory category, Pageable pageable) {
        log.info("Пришел GET запрос с параметрами category={} и pageable={}", category, pageable);
        final Collection<ProductDto> products = shoppingStoreService.getByCategory(category, pageable);
        log.info("Отправлен ответ на GET запрос с отелом {}", products);
        return products;
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        log.info("Пришел PUT запрос с телом {}", productDto);
        final ProductDto addedProduct = shoppingStoreService.addProduct(productDto);
        log.info("Отправлен ответ на PUT запрос с телом {}", addedProduct);
        return addedProduct;
    }

    @Override
    public ProductDto update(ProductDto productDto) {
        log.info("Пришел POST запрос с телом {}", productDto);
        final ProductDto updatedProduct = shoppingStoreService.update(productDto);
        log.info("Отправлен ответ на POST запрос с телом {}", updatedProduct);
        return updatedProduct;
    }

    @Override
    public Boolean removeProduct(UUID productId) {
        log.info("Пришел POST запрос /removeProductFromStore с id={}", productId);
        final Boolean removeResult = shoppingStoreService.removeProduct(productId);
        log.info("Отправлен ответ на POST запрос /removeProductFromStore с id={} и результатом {}", productId, removeResult);
        return removeResult;
    }

    @Override
    public Boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        log.info("Пришел POST запрос /quantityState с телом {}", request);
        final Boolean updatedProduct = shoppingStoreService.setProductQuantityState(request);
        log.info("Отправлен ответ на POST запрос с телом {}", updatedProduct);
        return updatedProduct;
    }

    @Override
    public ProductDto getById(UUID productId) {
        log.info("Пришел GET запрос с id={}", productId);
        final ProductDto foundProduct = shoppingStoreService.getById(productId);
        log.info("Отправлен ответ на GET запрос с id={} с телом {}", productId, foundProduct);
        return foundProduct;
    }
}
