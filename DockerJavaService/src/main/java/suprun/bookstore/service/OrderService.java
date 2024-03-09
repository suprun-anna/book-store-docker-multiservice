package suprun.bookstore.service;

import java.util.List;
import suprun.bookstore.dto.order.CreateOrderRequestDto;
import suprun.bookstore.dto.order.OrderDto;
import suprun.bookstore.dto.order.UpdateOrderStatusRequestDto;
import suprun.bookstore.dto.orderitem.OrderItemDto;

public interface OrderService {
    OrderDto save(Long userId, CreateOrderRequestDto requestDto);

    List<OrderDto> findAll(Long userId);

    List<OrderItemDto> findAllOrderItemsByOrderId(Long userId, Long orderId);

    OrderItemDto findOrderItemById(Long userId, Long itemId, Long orderId);

    OrderDto update(UpdateOrderStatusRequestDto requestDto, Long orderId);
}
