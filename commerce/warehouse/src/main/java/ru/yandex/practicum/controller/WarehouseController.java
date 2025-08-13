package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.feign.client.WarehouseClient;
import ru.yandex.practicum.service.WarehouseService;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseClient {
    private final WarehouseService warehouseService;

    @Override
    public void putNewProduct(NewProductInWarehouseRequest request) {
        log.info("Пришел PUT запрос с телом {}", request);
        warehouseService.putNewProduct(request);
        log.info("Отработан PUT запрос с телом {}", request);
    }

    @Override
    public BookedProductsDto checkAvailable(ShoppingCartDto shoppingCartDto) {
        log.info("Пришел POST запрос /check с телом {}", shoppingCartDto);
        final BookedProductsDto bookedProductsDto = warehouseService.checkAvailable(shoppingCartDto);
        log.info("Отправлен ответ POST запрос /check с телом {}", bookedProductsDto);
        return bookedProductsDto;
    }

    @Override
    public void addNewProduct(AddProductToWarehouseRequest request) {
        log.info("Пришел POST запрос /add с телом {}", request);
        warehouseService.addNewProduct(request);
        log.info("Отработан POST запрос /add с телом {}", request);
    }

    @Override
    public AddressDto getAddress() {
        log.info("Пришел GET запрос /address");
        final AddressDto addressDto = warehouseService.getAddress();
        log.info("Отправлен ответ GET запрос /address с телом {}", addressDto);
        return addressDto;
    }
}
