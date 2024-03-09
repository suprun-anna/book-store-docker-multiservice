package suprun.bookstore.mapper;

import suprun.bookstore.config.MapperConfig;
import suprun.bookstore.dto.category.CategoryDto;
import suprun.bookstore.dto.category.CreateCategoryRequestDto;
import suprun.bookstore.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto categoryDto);
}
