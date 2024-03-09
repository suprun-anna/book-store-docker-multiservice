package suprun.bookstore.service;

import java.util.List;
import suprun.bookstore.dto.book.BookDto;
import suprun.bookstore.dto.book.BookDtoWithoutCategoryIds;
import suprun.bookstore.dto.book.BookSearchParameters;
import suprun.bookstore.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    List<BookDto> findAll();

    List<BookDto> findAll(Pageable pageable);

    BookDto getById(Long id);

    BookDtoWithoutCategoryIds getBookWithoutCategoryById(Long id);

    BookDto updateBookById(Long id, CreateBookRequestDto bookDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters params);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId);
}
