package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.ProductDtoMapper;
import ru.yandex.practicum.model.OrderBooking;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.storage.WarehouseRepository;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

@Service("warehouseServiceImpl")
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository repository;
    private final ProductDtoMapper mapper;

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    @Override
    public void createNewProduct(NewProductInWarehouseRequest request) {
        if (repository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException("Product " + request.getProductId() + " already exist.");
        }
        final Product product = mapper.mapFromDto(request);
        repository.save(product);
    }

    @Override
    public void sendOrderForDelivery(ShippedToDeliveryRequest request) {
        //
    }

    @Override
    public void addReturnedProducts(Map<UUID, Long> products) {
        products.keySet().stream()
                .map(p -> new AddProductToWarehouseRequest(p, products.get(p)))
                .forEach(this::addProducts);
    }

    @Override
    public BookedProductsDto checkProductsAvailability(ShoppingCartDto shoppingCartDto) {
        double deliveryWeight = 0;
        double deliveryVolume = 0;
        boolean fragile = false;

        final Map<UUID, Long> products = shoppingCartDto.getProducts();
        for (UUID productUUID : products.keySet()) {
            final Product product = repository.findById(productUUID).orElseThrow(
                    () -> new ProductInShoppingCartLowQuantityInWarehouse("Product " + productUUID + " doesn't exist.")
            );
            if (product.getQuantity() < products.get(productUUID)) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Product " + productUUID + " has less quantity (" + product.getQuantity() + ").");
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

    @Override
    @Transactional
    public BookedProductsDto assemblyOrderToDelivery(AssemblyProductsForOrderRequest request) {
        final Map<UUID, Long> products = request.getProducts();
        final BookedProductsDto bookedProductsDto = checkProductsAvailability(new ShoppingCartDto(null, products));

        for (UUID productUUID : products.keySet()) {
            final Product product = repository.findById(productUUID).orElseThrow(
                    () -> new ProductInShoppingCartLowQuantityInWarehouse("Product " + productUUID + " doesn't exist.")
            );
            product.setQuantity(product.getQuantity() - products.get(productUUID));
            repository.save(product);
            final OrderBooking orderBooking = new OrderBooking(productUUID, request.getOrderId(), null, );
        }

        return bookedProductsDto;
    }

    @Override
    public void addProducts(AddProductToWarehouseRequest request) {
        final Product product = repository.findById(request.getProductId()).orElseThrow(
                () ->  new NoSpecifiedProductInWarehouseException("Product " + request.getProductId() + " doesn't exist.")
        );
        product.setQuantity(product.getQuantity() + request.getQuantity());
        repository.save(product);
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
