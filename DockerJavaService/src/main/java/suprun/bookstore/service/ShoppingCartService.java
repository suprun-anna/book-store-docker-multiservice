package suprun.bookstore.service;

import suprun.bookstore.dto.cartitem.AddItemToCartRequestDto;
import suprun.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import suprun.bookstore.dto.shoppingcart.ShoppingCartDto;
import suprun.bookstore.model.ShoppingCart;
import suprun.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCart createShoppingCart(User user);

    ShoppingCartDto addBooksToCartByUserId(Long userId, AddItemToCartRequestDto requestDto);

    ShoppingCartDto getCartDtoByUserId(Long id);

    ShoppingCart getCartByUserId(Long id);

    ShoppingCartDto updateCartItemById(
            Long userId, Long cartItemId, UpdateCartItemRequestDto requestDto);

    ShoppingCartDto delete(Long userId, Long cartItemId);

    void clearShoppingCart(Long cartId);
}
