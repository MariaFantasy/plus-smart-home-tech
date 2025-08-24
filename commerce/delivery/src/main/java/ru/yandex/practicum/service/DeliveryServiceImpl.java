package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.DeliveryState;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ShippedToDeliveryRequest;
import ru.yandex.practicum.exception.NoDeliveryFoundException;
import ru.yandex.practicum.feign.client.OrderClient;
import ru.yandex.practicum.feign.client.WarehouseClient;
import ru.yandex.practicum.mapper.DeliveryDtoMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.storage.DeliveryRepository;

import java.util.UUID;

@Service("deliveryServiceImpl")
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryDtoMapper deliveryDtoMapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Override
    public DeliveryDto create(DeliveryDto deliveryDto) {
        final Delivery delivery = deliveryDtoMapper.mapFromDto(deliveryDto);
        delivery.setState(DeliveryState.CREATED);
        final Delivery createdDelivery = deliveryRepository.save(delivery);
        return deliveryDtoMapper.mapToDto(createdDelivery);
    }

    @Override
    public Double calculateDeliveryCost(OrderDto orderDto) {
        final Delivery delivery = deliveryRepository.findByOrderId(orderDto.getOrderId()).orElseThrow(
                () ->  new NoDeliveryFoundException("Delivery for orderId " + orderDto.getOrderId() + " doesn't exist.")
        );

        double price = 5.0;

        if (delivery.getFromAddress().getCountry().equals("ADDRESS_1")) {
            price += price;
        } else if (delivery.getFromAddress().getCountry().equals("ADDRESS_2")) {
            price += price * 2;
        }

        if (orderDto.getFragile()) {
            price += price * 0.2;
        }

        price += orderDto.getDeliveryWeight() * 0.3;
        price += orderDto.getDeliveryVolume() * 0.2;

        if (!delivery.getFromAddress().getStreet().equals(delivery.getToAddress().getStreet())) {
            price += price * 0.2;
        }
        return price;
    }

    @Override
    public void picked(UUID orderId) {
        final Delivery delivery = deliveryRepository.findByOrderId(orderId).orElseThrow(
                () ->  new NoDeliveryFoundException("Delivery for orderId " + orderId + " doesn't exist.")
        );
        delivery.setState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);
        warehouseClient.sendOrderForDelivery(new ShippedToDeliveryRequest(orderId, delivery.getDeliveryId()));
    }

    @Override
    public void setStatusCancelled(UUID orderId) {
        final Delivery delivery = deliveryRepository.findByOrderId(orderId).orElseThrow(
                () ->  new NoDeliveryFoundException("Delivery for orderId " + orderId + " doesn't exist.")
        );
        delivery.setState(DeliveryState.CANCELLED);
        deliveryRepository.save(delivery);
        orderClient.sendFailedDelivery(orderId);
    }

    @Override
    public void setStatusSuccess(UUID orderId) {
        final Delivery delivery = deliveryRepository.findByOrderId(orderId).orElseThrow(
                () ->  new NoDeliveryFoundException("Delivery for orderId " + orderId + " doesn't exist.")
        );
        delivery.setState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);
        orderClient.completeOrder(orderId);
    }

    @Override
    public void setStatusFailed(UUID orderId) {
        final Delivery delivery = deliveryRepository.findByOrderId(orderId).orElseThrow(
                () ->  new NoDeliveryFoundException("Delivery for orderId " + orderId + " doesn't exist.")
        );
        delivery.setState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);
        orderClient.sendFailedDelivery(orderId);
    }
}
