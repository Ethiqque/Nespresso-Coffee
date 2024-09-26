package com.nespresso.order.api;

import com.stripe.model.checkout.Session;
import com.nespresso.cart.api.ShoppingCartProvider;
import com.nespresso.cart.repository.ShoppingCartRepository;
import com.nespresso.openapi.dto.OrderStatus;
import com.nespresso.openapi.dto.ShoppingCartDto;
import com.nespresso.order.converter.OrderDtoConverter;
import com.nespresso.order.entity.Order;
import com.nespresso.order.entity.OrderItem;
import com.nespresso.order.repository.OrderRepository;
import com.nespresso.user.api.SingleUserProvider;
import com.nespresso.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCreator {

    private final OrderRepository orderRepository;
    private final OrderProvider orderProvider;
    private final ShoppingCartProvider shoppingCartProvider;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderDtoConverter orderDtoConverter;
    private final SingleUserProvider singleUserProvider;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public boolean createOrderAndDeleteCart(Session stripeSession) {
        String sessionId = stripeSession.getId();
        log.info("Handling completion of stripe checkout session with id = '{}'.", sessionId);

        UUID userId = UUID.fromString(stripeSession.getMetadata().get("userId"));

        Optional<Order> existingOrder = orderProvider.getOrderEntityByUserAndSession(userId, sessionId);
        if (existingOrder.isPresent()) {
            log.info("Session completion has been already handled, order has been created with id {} ", existingOrder.get().getId());
            return false;
        }

        ShoppingCartDto shoppingCartDto = shoppingCartProvider.getByUserIdOrThrow(userId);
        UserEntity user = singleUserProvider.getUserEntityById(userId);

        log.info("Creating new order for user with id = '{}'", userId);
        Order orderEntity = createOrderEntity(user, shoppingCartDto, sessionId);
        orderRepository.saveAndFlush(orderEntity);
        log.info("New order with id = '{}' was created and saved to database for user with id = '{}'", orderEntity.getId(), userId);

        shoppingCartRepository.deleteByUserId(userId);
        log.info("Deleted the shopping cart for user with id = '{}'", userId);

        return true;
    }

    public Order createOrderEntity(final UserEntity user, final ShoppingCartDto shoppingCartDto, final String sessionId) {
        List<OrderItem> shoppingOrderItems = shoppingCartDto.getItems().stream()
                .map(orderDtoConverter::toOrderItem)
                .toList();

        return Order.builder()
                .userId(user.getId())
                .sessionId(sessionId)
                .status(OrderStatus.CREATED)
                .deliveryAddress(user.getAddress())
                .itemsQuantity(shoppingCartDto.getItemsQuantity())
                .itemsTotalPrice(shoppingCartDto.getItemsTotalPrice())
                .items(shoppingOrderItems)
                .build();
    }
}
