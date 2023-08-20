package by.teachmeskills.springbootproject.services.implementation;

import by.teachmeskills.springbootproject.constants.PagesPaths;
import by.teachmeskills.springbootproject.constants.RequestAttributesNames;
import by.teachmeskills.springbootproject.entities.Cart;
import by.teachmeskills.springbootproject.entities.Product;
import by.teachmeskills.springbootproject.entities.SearchCriteria;
import by.teachmeskills.springbootproject.exceptions.UserAlreadyExistsException;
import by.teachmeskills.springbootproject.repositories.CategoryRepository;
import by.teachmeskills.springbootproject.repositories.ProductRepository;
import by.teachmeskills.springbootproject.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ModelAndView getCategoryProducts(String category) {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.CATEGORY_PAGE);
        List<Product> products = productRepository.getCategoryProducts(category);
        modelAndView.addObject(RequestAttributesNames.CATEGORY_PRODUCTS, products);
        return modelAndView;
    }

    @Override
    public ModelAndView getProductById(int id) {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.PRODUCT_PAGE);
        Product product = productRepository.getProductById(id);
        modelAndView.addObject(product.getName());
        modelAndView.addObject(product);
        return modelAndView;
    }

    @Override
    public ModelAndView findProducts(SearchCriteria searchCriteria) {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.SEARCH_PAGE);
        if (searchCriteria.getPaginationNumber() < 1) {
            searchCriteria.setPaginationNumber(1);
        }
        modelAndView.addObject(RequestAttributesNames.PRODUCTS, productRepository.findProducts(searchCriteria.getKeyWords(), searchCriteria.getPaginationNumber()));
        modelAndView.addObject(RequestAttributesNames.CATEGORIES, categoryRepository.read());
        return modelAndView;
    }

    @Override
    public ModelAndView addProductToCart(int id, Cart cart) {
        ModelAndView modelAndView = new ModelAndView("redirect:/products/" + id);
        Product product = productRepository.getProductById(id);
        cart.addProduct(product);
        return modelAndView;
    }

    @Override
    @Transactional
    public ModelAndView create(Product product) throws UserAlreadyExistsException {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.PRODUCT_PAGE);
        productRepository.create(product);
        return modelAndView;
    }

    @Override
    public ModelAndView read() {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.SEARCH_PAGE);
        modelAndView.addObject(RequestAttributesNames.PRODUCTS, productRepository.read());
        modelAndView.addObject(RequestAttributesNames.CATEGORIES, categoryRepository.read());
        return modelAndView;
    }

    @Override
    @Transactional
    public Product update(Product product) {
        return productRepository.update(product);
    }

    @Override
    @Transactional
    public void delete(int id) {
        productRepository.delete(id);
    }
}
