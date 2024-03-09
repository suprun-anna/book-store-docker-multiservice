package suprun.bookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import suprun.bookstore.dto.cartitem.AddItemToCartRequestDto;
import suprun.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import suprun.bookstore.dto.shoppingcart.ShoppingCartDto;
import suprun.bookstore.mapper.CartItemMapper;
import suprun.bookstore.mapper.ShoppingCartMapper;
import suprun.bookstore.model.CartItem;
import suprun.bookstore.model.ShoppingCart;
import suprun.bookstore.model.User;
import suprun.bookstore.repository.cartitem.CartItemRepository;
import suprun.bookstore.repository.shoppingcart.ShoppingCartRepository;
import suprun.bookstore.repository.user.UserRepository;
import suprun.bookstore.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper cartMapper;

    @Override
    public ShoppingCart createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(new HashSet<>());
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto addBooksToCartByUserId(Long userId, AddItemToCartRequestDto requestDto) {
        ShoppingCart cartFromDb = getCartFromDb(userId);
        CartItem requestedItem = cartItemMapper.toModel(requestDto);
        requestedItem.setShoppingCart(cartFromDb);
        cartItemRepository.save(requestedItem);
        return cartMapper.toDto(getCartFromDb(userId));
    }

    @Override
    public ShoppingCart getCartByUserId(Long id) {
        return getCartFromDb(id);
    }

    @Override
    public ShoppingCartDto getCartDtoByUserId(Long id) {
        return cartMapper.toDto(getCartFromDb(id));
    }

    @Override
    public ShoppingCartDto updateCartItemById(
            Long userId, Long cartItemId, UpdateCartItemRequestDto requestDto) {
        CartItem itemFromDb = getItemFromDb(cartItemId);
        cartItemMapper.updateFromDto(requestDto, itemFromDb);
        cartItemRepository.save(itemFromDb);
        return cartMapper.toDto(getCartFromDb(userId));
    }

    @Override
    public ShoppingCartDto delete(Long userId, Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        return cartMapper.toDto(getCartFromDb(userId));
    }

    @Override
    public void clearShoppingCart(Long cartId) {
        cartItemRepository.deleteByShoppingCartId(cartId);
    }

    private CartItem getItemFromDb(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find item by id=" + id));
    }

    private ShoppingCart getCartFromDb(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    User userFromDb = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Can't find user by id=" + userId));
                    shoppingCart.setUser(userFromDb);
                    return shoppingCartRepository.save(shoppingCart);
                });
    }
}
