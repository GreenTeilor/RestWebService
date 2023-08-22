package by.teachmeskills.springbootproject.repositories;

import by.teachmeskills.springbootproject.entities.Category;

import java.util.Optional;

public interface CategoryRepository extends BaseRepository<Category> {
    Optional<Category> getCategoryByName(String name);
}
