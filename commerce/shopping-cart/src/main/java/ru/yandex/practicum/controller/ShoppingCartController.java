package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.feign.client.ShoppingCartClient;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController implements ShoppingCartClient {
    private final ShoppingCartService shoppingCartService;

    @Override
    public ShoppingCartDto get(String username) {
        log.info("Пришел GET запрос для пользователя {}", username);
        final ShoppingCartDto shoppingCartDto = shoppingCartService.get(username);
        log.info("Отправлен ответ на GET запрос с телом {}", shoppingCartDto);
        return shoppingCartDto;
    }

    @Override
    public ShoppingCartDto addProduct(String username, Map<UUID, Long> products) {
        log.info("Пришел PUT запрос для пользователя {} с телом {}", username, products);
        final ShoppingCartDto shoppingCartDto = shoppingCartService.addProduct(username, products);
        log.info("Отправлен ответ на PUT запрос с телом {}", shoppingCartDto);
        return shoppingCartDto;
    }

    @Override
    public void delete(String username) {
        log.info("Пришел DELETE запрос для пользователя {}", username);
        shoppingCartService.delete(username);
        log.info("Отработан запрос DELETE для пользователя {}", username);
    }

    @Override
    public ShoppingCartDto deleteProduct(String username, List<UUID> products) {
        log.info("Пришел POST запрос /remove для пользователя {} с телом {}", username, products);
        final ShoppingCartDto shoppingCartDto = shoppingCartService.deleteProduct(username, products);
        log.info("Отправлен ответ на POST запрос /remove для пользователя {} с телом {}", username, shoppingCartDto);
        return shoppingCartDto;
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        log.info("Пришел POST запрос /change-quantity для пользователя {} с телом {}", username, request);
        final ShoppingCartDto shoppingCartDto = shoppingCartService.changeQuantity(username, request);
        log.info("Отправлен ответ на POST запрос /change-quantity для пользователя {} с телом {}", username, shoppingCartDto);
        return shoppingCartDto;
    }

    @Override
    public String getUsername(UUID shoppingCartId) {
        log.info("Пришел GET запрос /username для корзины {}", shoppingCartId);
        final String username = shoppingCartService.getUsername(shoppingCartId);
        log.info("Отправлен ответ на GET запрос /username с телом {}", username);
        return username;
    }
}
