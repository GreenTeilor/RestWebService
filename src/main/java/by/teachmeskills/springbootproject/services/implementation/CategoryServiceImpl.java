package by.teachmeskills.springbootproject.services.implementation;

import by.teachmeskills.springbootproject.dto.CategoryDto;
import by.teachmeskills.springbootproject.dto.converters.CategoryConverter;
import by.teachmeskills.springbootproject.repositories.implementation.CategoryRepositoryImpl;
import by.teachmeskills.springbootproject.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepositoryImpl categoryRepository;
    private final CategoryConverter categoryConverter;

    @Override
    @Transactional
    public CategoryDto create(CategoryDto category) {
        return categoryConverter.toDto(categoryRepository.create(categoryConverter.fromDto(category)));
    }

    @Override
    public List<CategoryDto> read() {
        return categoryRepository.read().stream().map(categoryConverter::toDto).toList();
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto category) {
        return categoryConverter.toDto(categoryRepository.update(categoryConverter.fromDto(category)));
    }

    @Override
    @Transactional
    public void delete(int id) {
        categoryRepository.delete(id);
    }
}
