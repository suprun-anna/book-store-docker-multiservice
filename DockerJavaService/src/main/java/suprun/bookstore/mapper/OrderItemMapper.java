package suprun.bookstore.mapper;

import java.math.BigDecimal;
import suprun.bookstore.config.MapperConfig;
import suprun.bookstore.dto.orderitem.OrderItemDto;
import suprun.bookstore.model.CartItem;
import suprun.bookstore.model.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "quantity", source = "quantity")
    @Named("orderItemsToDto")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "order", ignore = true)
    OrderItem toOrderItemModel(CartItem cartItem);

    @AfterMapping
    default void getPriceOfOrderItem(@MappingTarget OrderItem orderItem, CartItem cartItem) {
        orderItem.setPrice(cartItem.getBook().getPrice().multiply(
                BigDecimal.valueOf(cartItem.getQuantity())));
    }
}
