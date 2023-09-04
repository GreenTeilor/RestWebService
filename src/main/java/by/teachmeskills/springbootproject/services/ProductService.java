package by.teachmeskills.springbootproject.services;

import by.teachmeskills.springbootproject.dto.ProductDto;
import by.teachmeskills.springbootproject.dto.CartDto;
import by.teachmeskills.springbootproject.dto.SearchCriteriaDto;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService extends BaseService<ProductDto>{
    List<ProductDto> getCategoryProducts(String category);
    ProductDto getProductById(int id) throws NoResourceFoundException;
    List<ProductDto> findProducts(SearchCriteriaDto searchCriteriaDto);
    CartDto addProductToCart(int id, CartDto cartDto) throws NoResourceFoundException;
    CartDto removeProductFromCart(int id, CartDto cartDto);
    CartDto removeAllProductsFromCart(CartDto cartDto);
    void saveToFile(List<ProductDto> products, HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;
    List<ProductDto> loadFromFile(MultipartFile file) throws IOException;
}
