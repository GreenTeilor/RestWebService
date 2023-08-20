package by.teachmeskills.springbootproject.repositories.implementation;

import by.teachmeskills.springbootproject.entities.Category;
import by.teachmeskills.springbootproject.repositories.CategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    private final static String GET_CATEGORIES_QUERY = "select c from Category c";

    @PersistenceContext
    private final EntityManager manager;

    @Override
    public Category create(Category category) {
        manager.persist(category);
        return category;
    }

    @Override
    public List<Category> read() {
        return manager.createQuery(GET_CATEGORIES_QUERY, Category.class).getResultList();
    }

    @Override
    public Category update(Category category) {
        return manager.merge(category);
    }

    @Override
    public void delete(int id) {
        Category category = manager.find(Category.class, id);
        manager.remove(category);
    }
}
