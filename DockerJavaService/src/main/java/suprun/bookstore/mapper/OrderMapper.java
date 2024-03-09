package suprun.bookstore.mapper;

import suprun.bookstore.config.MapperConfig;
import suprun.bookstore.dto.order.OrderDto;
import suprun.bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "orderItems",
            qualifiedByName = "orderItemsToDto")
    @Mapping(target = "orderDate", source = "orderDate")
    @Mapping(target = "total", source = "total")
    @Mapping(target = "status", source = "status")
    OrderDto toDto(Order order);
}
