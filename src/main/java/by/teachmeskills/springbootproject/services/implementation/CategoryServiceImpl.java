package by.teachmeskills.springbootproject.services.implementation;

import by.teachmeskills.springbootproject.constants.PagesPaths;
import by.teachmeskills.springbootproject.constants.RequestAttributesNames;
import by.teachmeskills.springbootproject.entities.Category;
import by.teachmeskills.springbootproject.repositories.implementation.CategoryRepositoryImpl;
import by.teachmeskills.springbootproject.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepositoryImpl categoryRepository;

    @Override
    @Transactional
    public ModelAndView create(Category category) {
        categoryRepository.create(category);
        return new ModelAndView(PagesPaths.CATEGORY_PAGE);
    }

    @Override
    public ModelAndView read() {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.HOME_PAGE);
        modelAndView.addObject(RequestAttributesNames.CATEGORIES, categoryRepository.read());
        return modelAndView;
    }

    @Override
    @Transactional
    public Category update(Category category) {
        return categoryRepository.update(category);
    }

    @Override
    @Transactional
    public void delete(int id) {
        categoryRepository.delete(id);
    }
}
