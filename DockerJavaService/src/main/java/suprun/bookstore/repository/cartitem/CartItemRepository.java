package suprun.bookstore.repository.cartitem;

import suprun.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.shoppingCart.id = :cartId")
    void deleteByShoppingCartId(Long cartId);
}
