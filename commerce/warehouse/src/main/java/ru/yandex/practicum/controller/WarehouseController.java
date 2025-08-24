package ru.yandex.practicum.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.feign.client.WarehouseClient;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseClient {
    private final WarehouseService warehouseService;

    @Override
    public void createNewProduct(NewProductInWarehouseRequest request) {
        log.info("Пришел PUT запрос с телом {}", request);
        warehouseService.createNewProduct(request);
        log.info("Отработан PUT запрос с телом {}", request);
    }

    @Override
    public void addProducts(AddProductToWarehouseRequest request) {
        log.info("Пришел POST запрос /add с телом {}", request);
        warehouseService.addProduct(request);
        log.info("Отработан POST запрос /add с телом {}", request);
    }

    @Override
    public void addReturnedProducts(@RequestBody @NotNull Map<UUID, Long> products) {
        log.info("Пришел POST запрос /return с телом {}", products);
        warehouseService.addReturnedProducts(products);
        log.info("Отработан POST запрос /return с телом {}", products);
    }

    @Override
    public BookedProductsDto checkProductsAvailability(ShoppingCartDto shoppingCartDto) {
        log.info("Пришел POST запрос /check с телом {}", shoppingCartDto);
        final BookedProductsDto bookedProductsDto = warehouseService.checkProductsAvailability(shoppingCartDto);
        log.info("Отправлен ответ POST запрос /check с телом {}", bookedProductsDto);
        return bookedProductsDto;
    }

    @Override
    public BookedProductsDto assemblyOrderToDelivery(@RequestBody AssemblyProductsForOrderRequest request) {
        log.info("Пришел POST запрос /assembly с телом {}", request);
        final BookedProductsDto bookedProductsDto = warehouseService.assemblyOrderToDelivery(request);
        log.info("Отработан POST запрос /assembly с телом {}", bookedProductsDto);
        return bookedProductsDto;
    }

    @Override
    public void sendOrderForDelivery(@RequestBody ShippedToDeliveryRequest request) {
        log.info("Пришел POST запрос /shipped с телом {}", request);
        warehouseService.sendOrderForDelivery(request);
        log.info("Отработан POST запрос /shipped с телом {}", request);
    }

    @Override
    public AddressDto getAddress() {
        log.info("Пришел GET запрос /address");
        final AddressDto addressDto = warehouseService.getAddress();
        log.info("Отправлен ответ GET запрос /address с телом {}", addressDto);
        return addressDto;
    }
}
