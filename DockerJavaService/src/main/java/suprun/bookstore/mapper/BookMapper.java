package suprun.bookstore.mapper;

import java.util.Optional;
import java.util.stream.Collectors;
import suprun.bookstore.config.MapperConfig;
import suprun.bookstore.dto.book.BookDto;
import suprun.bookstore.dto.book.BookDtoWithoutCategoryIds;
import suprun.bookstore.dto.book.CreateBookRequestDto;
import suprun.bookstore.model.Book;
import suprun.bookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto bookDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
    }

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        return Optional.ofNullable(id)
                .map(Book::new)
                .orElse(null);
    }
}
