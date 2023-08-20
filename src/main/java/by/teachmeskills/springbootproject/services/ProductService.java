package by.teachmeskills.springbootproject.services;

import by.teachmeskills.springbootproject.entities.Cart;
import by.teachmeskills.springbootproject.entities.Product;
import by.teachmeskills.springbootproject.entities.SearchCriteria;
import org.springframework.web.servlet.ModelAndView;

public interface ProductService extends BaseService<Product>{
    ModelAndView getCategoryProducts(String category);
    ModelAndView getProductById(int id);
    ModelAndView findProducts(SearchCriteria searchCriteria);
    ModelAndView addProductToCart(int id, Cart cart);
}
