package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.feign.client.OrderClient;
import ru.yandex.practicum.feign.client.ShoppingStoreClient;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.mapper.PaymentDtoMapper;
import ru.yandex.practicum.model.PaymentState;
import ru.yandex.practicum.storage.PaymentRepository;

import java.util.Map;
import java.util.UUID;

@Service("paymentServiceImpl")
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentDtoMapper paymentDtoMapper;
    private final OrderClient orderClient;
    private final ShoppingStoreClient shoppingStoreClient;

    public PaymentDto create(OrderDto orderDto) {
        final Payment payment = new Payment(
                null,
                orderDto.getOrderId(),
                orderDto.getTotalPrice(),
                orderDto.getDeliveryPrice(),
                orderDto.getProductPrice() * 0.1,
                PaymentState.PENDING
        );
        final Payment createdPayment = paymentRepository.save(payment);
        return paymentDtoMapper.mapToDto(createdPayment);
    }

    public Double calculateProductCost(OrderDto orderDto) {
        final Map<UUID, Long> products = orderDto.getProducts();
        double price = 0.;
        for (UUID productId : products.keySet()) {
            price += shoppingStoreClient.getById(productId).getPrice() * products.get(productId);
        }
        return price;
    }

    public Double calculateTotalCost(OrderDto orderDto) {
        double productCost = calculateProductCost(orderDto);
        double fee = productCost * 0.1;
        return productCost + fee + orderDto.getDeliveryPrice();
    }

    public void sendSuccessState(UUID orderId) {
        final Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new NoOrderFoundException("Order " + orderId + " hasn't any payment.")
        );
        payment.setState(PaymentState.SUCCESS);
        paymentRepository.save(payment);
        orderClient.payOrder(orderId);
    }

    public void sendFailedState(UUID orderId) {
        final Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new NoOrderFoundException("Order " + orderId + " hasn't any payment.")
        );
        payment.setState(PaymentState.FAILED);
        paymentRepository.save(payment);
        orderClient.sendFailedPayment(orderId);
    }
}
