package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.feign.client.DeliveryClient;
import ru.yandex.practicum.feign.client.PaymentClient;
import ru.yandex.practicum.feign.client.ShoppingCartClient;
import ru.yandex.practicum.feign.client.WarehouseClient;
import ru.yandex.practicum.mapper.OrderDtoMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.storage.OrderRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("orderServiceImpl")
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDtoMapper orderDtoMapper;
    private final ShoppingCartClient shoppingCartClient;
    private final WarehouseClient warehouseClient;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;

    @Override
    public Collection<OrderDto> getByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Username is empty.");
        }
        final Collection<Order> orders = orderRepository.findByUsername(username);
        return orders.stream()
                .map(orderDtoMapper::mapToDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public OrderDto create(CreateNewOrderRequest request) {
        final String username = shoppingCartClient.getUsername(request.getShoppingCart().getShoppingCartId());
        final BookedProductsDto bookedProductsDto = warehouseClient.checkProductsAvailability(request.getShoppingCart());

        final Order order = new Order(
            null,
            username,
            request.getShoppingCart().getShoppingCartId(),
            request.getShoppingCart().getProducts(),
            null,
            null,
            OrderState.NEW,
            bookedProductsDto.getDeliveryWeight(),
            bookedProductsDto.getDeliveryVolume(),
            bookedProductsDto.getFragile(),
            null,
            null,
            null
        );
        final Order createdOrder = orderRepository.save(order);

        final DeliveryDto delivery = deliveryClient.addDelivery(
                new DeliveryDto(
                        null,
                        warehouseClient.getAddress(),
                        request.getDeliveryAddress(),
                        createdOrder.getOrderId(),
                        DeliveryState.CREATED
                    )
        );
        createdOrder.setDeliveryId(delivery.getDeliveryId());
        orderRepository.save(createdOrder);

        calculateDeliveryPrice(order.getOrderId());
        calculateTotalPrice(order.getOrderId());

        final PaymentDto paymentDto = paymentClient.addPayment(orderDtoMapper.mapToDto(order));
        order.setPaymentId(paymentDto.getPaymentId());
        final Order finalOrder = orderRepository.save(order);

        return orderDtoMapper.mapToDto(finalOrder);
    }

    @Override
    public OrderDto calculateTotalPrice(UUID orderId) {
        calculateDeliveryPrice(orderId);
        final Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NoOrderFoundException("Order " + orderId + " doesn't exist.")
        );
        double productCost = paymentClient.calculateProductCost(orderDtoMapper.mapToDto(order));
        order.setProductPrice(productCost);
        double totalCost = paymentClient.calculateTotalCost(orderDtoMapper.mapToDto(order));
        order.setTotalPrice(totalCost);
        orderRepository.save(order);
        return orderDtoMapper.mapToDto(order);
    }

    @Override
    public OrderDto calculateDeliveryPrice(UUID orderId) {
        final Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NoOrderFoundException("Order " + orderId + " doesn't exist.")
        );
        double deliveryCost = deliveryClient.calculateDeliveryCost(orderDtoMapper.mapToDto(order));
        order.setDeliveryPrice(deliveryCost);
        orderRepository.save(order);
        return orderDtoMapper.mapToDto(order);
    }

    @Override
    public OrderDto pay(UUID orderId) {
        final Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NoOrderFoundException("Order " + orderId + " doesn't exist.")
        );
        order.setState(OrderState.PAID);
        orderRepository.save(order);
        return orderDtoMapper.mapToDto(order);
    }

    @Override
    public OrderDto sendFailedPayment(UUID orderId) {
        final Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NoOrderFoundException("Order " + orderId + " doesn't exist.")
        );
        order.setState(OrderState.PAYMENT_FAILED);
        orderRepository.save(order);
        return orderDtoMapper.mapToDto(order);
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        final Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NoOrderFoundException("Order " + orderId + " doesn't exist.")
        );
        order.setState(OrderState.ASSEMBLED);
        orderRepository.save(order);
        return orderDtoMapper.mapToDto(order);
    }

    @Override
    public OrderDto sendFailedAssembly(UUID orderId) {
        final Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NoOrderFoundException("Order " + orderId + " doesn't exist.")
        );
        order.setState(OrderState.ASSEMBLY_FAILED);
        orderRepository.save(order);
        return orderDtoMapper.mapToDto(order);
    }

    @Override
    public OrderDto sendToDelivery(UUID orderId) {
        final Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NoOrderFoundException("Order " + orderId + " doesn't exist.")
        );
        order.setState(OrderState.DELIVERED);
        orderRepository.save(order);
        return orderDtoMapper.mapToDto(order);
    }

    @Override
    public OrderDto sendFailedDelivery(UUID orderId) {
        final Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NoOrderFoundException("Order " + orderId + " doesn't exist.")
        );
        order.setState(OrderState.DELIVERY_FAILED);
        orderRepository.save(order);
        return orderDtoMapper.mapToDto(order);
    }

    @Override
    public OrderDto completeOrder(UUID orderId) {
        final Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NoOrderFoundException("Order " + orderId + " doesn't exist.")
        );
        order.setState(OrderState.COMPLETED);
        orderRepository.save(order);
        return orderDtoMapper.mapToDto(order);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {
            final Order order = orderRepository.findById(request.getOrderId()).orElseThrow(
                () -> new NoOrderFoundException("Order " + request.getOrderId() + " doesn't exist.")
        );
        order.setState(OrderState.PRODUCT_RETURNED);
        orderRepository.save(order);
        warehouseClient.addReturnedProducts(request.getProducts());
        return orderDtoMapper.mapToDto(order);
    }
}
