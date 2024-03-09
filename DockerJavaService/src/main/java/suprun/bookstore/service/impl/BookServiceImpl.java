package suprun.bookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import suprun.bookstore.dto.book.BookDto;
import suprun.bookstore.dto.book.BookDtoWithoutCategoryIds;
import suprun.bookstore.dto.book.BookSearchParameters;
import suprun.bookstore.dto.book.CreateBookRequestDto;
import suprun.bookstore.mapper.BookMapper;
import suprun.bookstore.model.Book;
import suprun.bookstore.model.Category;
import suprun.bookstore.repository.book.BookRepository;
import suprun.bookstore.repository.book.BookSpecificationBuilder;
import suprun.bookstore.repository.category.CategoryRepository;
import suprun.bookstore.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        if (bookDto.categoryIds() != null) {
            Set<Category> categories = getCategoriesByIds(bookDto.categoryIds());
            book.setCategories(categories);
        }
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id=" + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDtoWithoutCategoryIds getBookWithoutCategoryById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id=" + id));
        return bookMapper.toDtoWithoutCategories(book);
    }

    @Override
    public BookDto updateBookById(Long id, CreateBookRequestDto bookDto) {
        bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't update book by id=" + id));
        Book book = bookMapper.toEntity(bookDto);
        book.setId(id);
        if (bookDto.categoryIds() != null) {
            Set<Category> categories = getCategoriesByIds(bookDto.categoryIds());
            book.setCategories(categories);
        }
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParameters params) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId) {
        return bookRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }

    private Set<Category> getCategoriesByIds(Set<Long> categoryIds) {
        Set<Category> categories = new HashSet<>();
        for (Long categoryId : categoryIds) {
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            optionalCategory.ifPresent(categories::add);
        }
        return categories;
    }
}
