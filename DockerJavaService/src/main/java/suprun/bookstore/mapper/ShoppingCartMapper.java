package suprun.bookstore.mapper;

import suprun.bookstore.config.MapperConfig;
import suprun.bookstore.dto.shoppingcart.ShoppingCartDto;
import suprun.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCart toModel(ShoppingCartDto shoppingCartResponseDto);
}
