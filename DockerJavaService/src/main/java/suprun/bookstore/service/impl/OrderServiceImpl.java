package suprun.bookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import suprun.bookstore.dto.order.CreateOrderRequestDto;
import suprun.bookstore.dto.order.OrderDto;
import suprun.bookstore.dto.order.UpdateOrderStatusRequestDto;
import suprun.bookstore.dto.orderitem.OrderItemDto;
import suprun.bookstore.mapper.OrderItemMapper;
import suprun.bookstore.mapper.OrderMapper;
import suprun.bookstore.model.Order;
import suprun.bookstore.model.OrderItem;
import suprun.bookstore.model.ShoppingCart;
import suprun.bookstore.model.Status;
import suprun.bookstore.repository.order.OrderRepository;
import suprun.bookstore.repository.orderitem.OrderItemRepository;
import suprun.bookstore.service.OrderService;
import suprun.bookstore.service.ShoppingCartService;
import suprun.bookstore.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    @Override
    public OrderDto save(Long userId, CreateOrderRequestDto requestDto) {
        ShoppingCart cartFromDb = shoppingCartService.getCartByUserId(userId);
        if (cartFromDb.getCartItems().size() == 0) {
            throw new IllegalStateException("Cannot proceed with an empty shopping cart.");
        }
        Order order = createOrderWithoutItems(userId, requestDto);
        order = orderRepository.save(order);
        Set<OrderItem> orderItems = createOrderItemsSet(cartFromDb, order);
        order.setOrderItems(orderItems);
        order.setTotal(order.getOrderItems().stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        shoppingCartService.clearShoppingCart(cartFromDb.getId());
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> findAll(Long userId) {
        return orderRepository.findAllByUserId(userId).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> findAllOrderItemsByOrderId(Long userId, Long orderId) {
        Order orderFromDb = getOrderFromDbByUserIdAndOrderId(userId, orderId);
        return orderFromDb.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto findOrderItemById(Long userId, Long itemId, Long orderId) {
        Order orderFromDb = getOrderFromDbByUserIdAndOrderId(userId, orderId);
        return orderFromDb.getOrderItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .map(orderItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find item by itemId=" + itemId));
    }

    @Override
    public OrderDto update(UpdateOrderStatusRequestDto requestDto, Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new EntityNotFoundException("Can't find order by orderId=" + orderId);
        } else {
            Order order = optionalOrder.get();
            order.setStatus(requestDto.status());
            return orderMapper.toDto(order);
        }
    }

    private Order getOrderFromDbByUserIdAndOrderId(Long userId, Long orderId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order by userId=" + userId + " and orderId=" + orderId));
    }

    private Order createOrderWithoutItems(Long userId, CreateOrderRequestDto requestDto) {
        Order order = new Order();
        order.setUser(userService.getById(userId));
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.shippingAddress());
        order.setTotal(BigDecimal.ZERO);
        return order;
    }

    private Set<OrderItem> createOrderItemsSet(ShoppingCart shoppingCart, Order order) {
        return shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = orderItemMapper.toOrderItemModel(cartItem);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .peek(orderItemRepository::save)
                .collect(Collectors.toSet());
    }
}
