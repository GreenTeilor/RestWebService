package by.teachmeskills.springbootproject.repositories.implementation;

import by.teachmeskills.springbootproject.entities.Product;
import by.teachmeskills.springbootproject.repositories.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final static String GET_CATEGORY_PRODUCTS_QUERY = "select p from Product p where p.category.name=:category";
    private final static String GET_PRODUCTS_QUERY = "select p from Product p";
    private final static String SEARCH_PRODUCTS_QUERY = "select p from Product p where p.name like :name or p.description like :description order by p.name asc";

    @PersistenceContext
    private final EntityManager manager;

    @Override
    public List<Product> getCategoryProducts(String category) {
        TypedQuery<Product> query = manager.createQuery(GET_CATEGORY_PRODUCTS_QUERY, Product.class);
        query.setParameter("category", category);
        return query.getResultList();
    }

    @Override
    public Product getProductById(int id) {
        return manager.find(Product.class, id);
    }

    @Override
    public List<Product> findProducts(String keyWords, int pageNumber) {
        TypedQuery<Product> query = manager.createQuery(SEARCH_PRODUCTS_QUERY, Product.class);
        String searchPattern = "%" + keyWords.trim() + "%";
        query.setParameter("name", searchPattern);
        query.setParameter("description", searchPattern);
        query.setFirstResult((pageNumber - 1) * 3);
        query.setMaxResults(3);
        return query.getResultList();
    }

    @Override
    public Product create(Product product) {
        manager.persist(product);
        return product;
    }

    @Override
    public List<Product> read() {
        return manager.createQuery(GET_PRODUCTS_QUERY, Product.class).getResultList();
    }

    @Override
    public Product update(Product product) {
        return manager.merge(product);
    }

    @Override
    public void delete(int id) {
        Product user = manager.find(Product.class, id);
        manager.remove(user);
    }
}
