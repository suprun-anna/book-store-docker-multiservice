package suprun.bookstore.mapper;

import suprun.bookstore.config.MapperConfig;
import suprun.bookstore.dto.cartitem.AddItemToCartRequestDto;
import suprun.bookstore.dto.cartitem.CartItemDto;
import suprun.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import suprun.bookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toModel(AddItemToCartRequestDto requestDto);

    @Mapping(target = "book", ignore = true)
    void updateFromDto(UpdateCartItemRequestDto requestDto, @MappingTarget CartItem itemFromDb);
}
