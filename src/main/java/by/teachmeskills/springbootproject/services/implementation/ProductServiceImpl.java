package by.teachmeskills.springbootproject.services.implementation;

import by.teachmeskills.springbootproject.dto.ProductDto;
import by.teachmeskills.springbootproject.dto.converters.ProductConverter;
import by.teachmeskills.springbootproject.dto.CartDto;
import by.teachmeskills.springbootproject.entities.Product;
import by.teachmeskills.springbootproject.dto.SearchCriteriaDto;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;
import by.teachmeskills.springbootproject.repositories.ProductRepository;
import by.teachmeskills.springbootproject.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    @Override
    public List<ProductDto> getCategoryProducts(String category) {
        return productRepository.getCategoryProducts(category).stream().map(productConverter::toDto).toList();
    }

    @Override
    public ProductDto getProductById(int id) throws NoResourceFoundException {
        return Optional.ofNullable(productConverter.toDto(productRepository.getProductById(id))).
                orElseThrow(() -> new NoResourceFoundException("Product with id " + id + " not found"));
    }

    @Override
    public List<ProductDto> findProducts(SearchCriteriaDto searchCriteriaDto) {
        if (searchCriteriaDto.getPaginationNumber() < 1) {
            searchCriteriaDto.setPaginationNumber(1);
        }
        return productRepository.findProducts(searchCriteriaDto.getKeyWords(), searchCriteriaDto.getPaginationNumber()).
                stream().map(productConverter::toDto).toList();
    }

    @Override
    public CartDto addProductToCart(int id, CartDto cartDto) throws NoResourceFoundException {
        Product product = Optional.ofNullable(productRepository.getProductById(id)).
                orElseThrow(() -> new NoResourceFoundException("No product with id: " + id + " found"));
        cartDto.addProduct(productConverter.toDto(product));
        return cartDto;
    }

    @Override
    public CartDto removeProductFromCart(int id, CartDto cartDto) {
        cartDto.removeProduct(id);
        return cartDto;
    }

    @Override
    public CartDto removeAllProductsFromCart(CartDto cartDto) {
        cartDto.clear();
        return cartDto;
    }

    @Override
    @Transactional
    public ProductDto create(ProductDto product) {
        return productConverter.toDto(productRepository.create(productConverter.fromDto(product)));
    }

    @Override
    public List<ProductDto> read() {
        return productRepository.read().stream().map(productConverter::toDto).toList();
    }

    @Override
    @Transactional
    public ProductDto update(ProductDto product) {
        return productConverter.toDto(productRepository.update(productConverter.fromDto(product)));
    }

    @Override
    @Transactional
    public void delete(int id) {
        productRepository.delete(id);
    }
}
