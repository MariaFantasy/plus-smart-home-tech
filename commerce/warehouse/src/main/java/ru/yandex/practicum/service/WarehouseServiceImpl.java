package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.*;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service("warehouseServiceImpl")
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final OrderBookingService orderBookingService;
    private final ProductService productService;

    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    @Override
    public void createNewProduct(NewProductInWarehouseRequest request) {
        productService.create(request);
    }

    @Override
    public void addProduct(AddProductToWarehouseRequest request) {
        productService.add(request);
    }

    @Override
    public void addReturnedProducts(Map<UUID, Long> products) {
        products.keySet().stream()
                .map(p -> new AddProductToWarehouseRequest(p, products.get(p)))
                .forEach(productService::add);
    }

    @Override
    public BookedProductsDto checkProductsAvailability(ShoppingCartDto shoppingCartDto) {
        return productService.checkProductsAvailability(shoppingCartDto.getProducts());
    }

    @Override
    @Transactional
    public BookedProductsDto assemblyOrderToDelivery(AssemblyProductsForOrderRequest request) {
        final BookedProductsDto bookedProductsDto = productService.checkProductsAvailability(request.getProducts());
        orderBookingService.create(request);
        return bookedProductsDto;
    }

    @Override
    public void sendOrderForDelivery(ShippedToDeliveryRequest request) {
        orderBookingService.sendToDelivery(request);
    }

    @Override
    public AddressDto getAddress() {
        return new AddressDto(
                CURRENT_ADDRESS,
                CURRENT_ADDRESS,
                CURRENT_ADDRESS,
                CURRENT_ADDRESS,
                CURRENT_ADDRESS
        );
    }
}
