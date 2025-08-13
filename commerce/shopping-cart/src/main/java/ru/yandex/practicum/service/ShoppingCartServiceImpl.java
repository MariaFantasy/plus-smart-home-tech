package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.mapper.ShoppingCartDtoMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.storage.ShoppingCartRepository;

import java.util.*;

@Service("shoppingCartServiceImpl")
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository repository;
    private final ShoppingCartDtoMapper mapper;

    @Override
    public ShoppingCartDto get(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Username is empty.");
        }
        final Optional<ShoppingCart> optionalShoppingCart = repository.findByUsername(username);
        final ShoppingCart shoppingCart = optionalShoppingCart.orElse(new ShoppingCart(null, username, true, new HashMap<>()));
        repository.save(shoppingCart);
        final ShoppingCart resultShoppingCart = repository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("Shopping cart for " + username + " doesn't exist.")
        );
        return mapper.mapToDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addProduct(String username, Map<UUID, Long> products) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Username is empty.");
        }
        final Optional<ShoppingCart> optionalShoppingCart = repository.findByUsername(username);
        final ShoppingCart shoppingCartToSave = optionalShoppingCart.orElse(new ShoppingCart(null, username, true, new HashMap<>()));
        repository.save(shoppingCartToSave);
        final ShoppingCart shoppingCart = repository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("Shopping cart for " + username + " doesn't exist.")
        );
        products.keySet()
                .forEach(product -> {
                    if (shoppingCart.getProducts().containsKey(product)) {
                        shoppingCart.getProducts().put(product, shoppingCart.getProducts().get(product) + products.get(product));
                    } else {
                        shoppingCart.getProducts().put(product, products.get(product));
                    }
                });
        repository.save(shoppingCart);
        final ShoppingCart resultShoppingCart = repository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("Shopping cart for " + username + " doesn't exist.")
        );
        return mapper.mapToDto(resultShoppingCart);
    }

    @Override
    public void delete(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Username is empty.");
        }
        final ShoppingCart shoppingCart = repository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("Shopping cart for " + username + " doesn't exist.")
        );
        shoppingCart.setIsActive(false);
        repository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto deleteProduct(String username, List<UUID> products) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Username is empty.");
        }
        final ShoppingCart shoppingCart = repository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("Shopping cart for " + username + " doesn't exist.")
        );
        products.stream()
                .forEach(product -> {
                    if (!shoppingCart.getProducts().containsKey(product)) {
                        throw new NoProductsInShoppingCartException("Product " + product + " not exists in shopping cart for user " + username);
                    }
                    shoppingCart.getProducts().remove(product);
                });
        repository.save(shoppingCart);
        final ShoppingCart resultShoppingCart = repository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("Shopping cart for " + username + " doesn't exist.")
        );
        return mapper.mapToDto(resultShoppingCart);
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Username is empty.");
        }
        final ShoppingCart shoppingCart = repository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("Shopping cart for " + username + " doesn't exist.")
        );
        if (!shoppingCart.getProducts().containsKey(request.getProductId())) {
            throw new NoProductsInShoppingCartException("Product " + request.getProductId() + " not exists in shopping cart for user " + username);
        }
        shoppingCart.getProducts().put(request.getProductId(), request.getNewQuantity());
        repository.save(shoppingCart);
        final ShoppingCart resultShoppingCart = repository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("Shopping cart for " + username + " doesn't exist.")
        );
        return mapper.mapToDto(resultShoppingCart);
    }
}
