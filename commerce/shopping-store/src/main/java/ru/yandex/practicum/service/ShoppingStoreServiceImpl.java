package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.mapper.ProductDtoMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.storage.ShoppingStoreRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("shoppingStoreServiceImpl")
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ShoppingStoreRepository repository;
    private final ProductDtoMapper mapper;

    @Override
    public Collection<ProductDto> getByCategory(ProductCategory category, Pageable pageable) {
        final Collection<Product> products = repository.findByCategory(category, pageable);
        return products.stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        final Product product = mapper.mapFromDto(productDto);
        repository.save(product);
        final Product savedProduct = repository.findById(product.getId()).orElseThrow(
                () -> new ProductNotFoundException("Product with id = " + product.getId() + " not found.")
        );
        return mapper.mapToDto(savedProduct);
    }

    @Override
    public ProductDto update(ProductDto productDto) {
        final Product product = repository.findById(productDto.getProductId()).orElseThrow(
                () -> new ProductNotFoundException("Product with id = " + productDto.getProductId() + " not found.")
        );
        final Product productToUpdate = mapper.mapFromDto(productDto);
        repository.save(productToUpdate);
        final Product updatedProduct = repository.findById(productToUpdate.getId()).orElseThrow(
                () -> new ProductNotFoundException("Product with id = " + productToUpdate.getId() + " not found.")
        );
        return mapper.mapToDto(updatedProduct);
    }

    @Override
    public Boolean removeProduct(UUID productId) {
        final Product product = repository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product with id = " + productId + " not found.")
        );
        product.setProductState(ProductState.DEACTIVATE);
        repository.save(product);
        return true;
    }

    @Override
    public Boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        final Product product = repository.findById(request.getProductId()).orElseThrow(
                () -> new ProductNotFoundException("Product with id = " + request.getProductId() + " not found.")
        );
        product.setQuantityState(request.getQuantityState());
        repository.save(product);
        return true;
    }

    @Override
    public ProductDto getById(UUID productId) {
        final Product product = repository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product with id = " + productId + " not found.")
        );
        return mapper.mapToDto(product);
    }
}
