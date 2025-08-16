package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.ShippedToDeliveryRequest;
import ru.yandex.practicum.model.OrderBooking;
import ru.yandex.practicum.storage.OrderBookingRepository;

import java.util.Map;
import java.util.UUID;

@Service("orderBookingServiceImpl")
@RequiredArgsConstructor
public class OrderBookingServiceImpl implements OrderBookingService {
    private final ProductService productService;
    private final OrderBookingRepository orderBookingRepository;

    @Override
    public void create(AssemblyProductsForOrderRequest request) {
        final Map<UUID, Long> products = request.getProducts();
        products.keySet()
                .forEach(productId -> productService.reserve(productId, products.get(productId)));

        final OrderBooking orderBooking = new OrderBooking(
                null,
                request.getOrderId(),
                null,
                products
        );
        orderBookingRepository.save(orderBooking);
    }

    @Override
    public void sendToDelivery(ShippedToDeliveryRequest request) {
        final OrderBooking orderBooking = orderBookingRepository.findByOrderId(request.getOrderId()).orElseThrow(
                () -> new NotFoundException("Order " + request.getOrderId() + " doesn't exist.")
        );
        orderBooking.setDeliveryId(request.getDeliveryId());
        orderBookingRepository.save(orderBooking);
    }
}
