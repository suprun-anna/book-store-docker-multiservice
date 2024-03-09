package suprun.bookstore.dto.shoppingcart;

import java.util.Set;
import suprun.bookstore.dto.cartitem.CartItemDto;

public record ShoppingCartDto(
        Long id,
        Long userId,
        Set<CartItemDto> cartItems
){
}
