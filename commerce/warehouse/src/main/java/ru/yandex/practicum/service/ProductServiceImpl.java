package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.ProductDtoMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.storage.ProductRepository;

import java.util.Map;
import java.util.UUID;

@Service("productServiceImpl")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductDtoMapper productDtoMapper;

    @Override
    public void create(NewProductInWarehouseRequest request) {
        if (productRepository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException("Product " + request.getProductId() + " already exist.");
        }
        final Product product = productDtoMapper.mapFromDto(request);
        productRepository.save(product);
    }

    @Override
    public void add(AddProductToWarehouseRequest request) {
        final Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () ->  new NoSpecifiedProductInWarehouseException("Product " + request.getProductId() + " doesn't exist.")
        );
        product.setQuantity(product.getQuantity() + request.getQuantity());
        productRepository.save(product);
    }

    @Override
    public void reserve(UUID productId, Long quantity) {
        final Product product = productRepository.findById(productId).orElseThrow(
                () ->  new NoSpecifiedProductInWarehouseException("Product " + productId + " doesn't exist.")
        );
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    @Override
    public BookedProductsDto checkProductsAvailability(Map<UUID, Long> products) {
        double deliveryWeight = 0;
        double deliveryVolume = 0;
        boolean fragile = false;

        for (UUID productId : products.keySet()) {
            final Product product = productRepository.findById(productId).orElseThrow(
                    () -> new ProductInShoppingCartLowQuantityInWarehouse("Product " + productId + " doesn't exist.")
            );
            if (product.getQuantity() < products.get(productId)) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Product " + productId + " has less quantity (" + product.getQuantity() + ").");
            }
            deliveryWeight += product.getWeight();
            deliveryVolume += product.getWidth() * product.getHeight() * product.getDepth();
            fragile |= product.getFragile();
        }

        return new BookedProductsDto(
                deliveryWeight,
                deliveryVolume,
                fragile
        );
    }
}
