package by.teachmeskills.springbootproject.repositories;

import by.teachmeskills.springbootproject.entities.Product;

import java.util.List;

public interface ProductRepository extends BaseRepository<Product> {
    List<Product> getCategoryProducts(String category);

    Product getProductById(int id);

    List<Product> findProducts(String keyWords, int pageNumber);
}
