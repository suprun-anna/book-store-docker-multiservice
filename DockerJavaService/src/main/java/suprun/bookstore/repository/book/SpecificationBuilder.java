package suprun.bookstore.repository.book;

import suprun.bookstore.dto.book.BookSearchParameters;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParameters params);
}
