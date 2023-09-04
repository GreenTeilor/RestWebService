package by.teachmeskills.springbootproject.services.implementation;

import by.teachmeskills.springbootproject.dto.PagingParamsDto;
import by.teachmeskills.springbootproject.dto.ProductDto;
import by.teachmeskills.springbootproject.dto.converters.ProductConverter;
import by.teachmeskills.springbootproject.dto.CartDto;
import by.teachmeskills.springbootproject.entities.Product;
import by.teachmeskills.springbootproject.dto.SearchCriteriaDto;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;
import by.teachmeskills.springbootproject.repositories.ProductRepository;
import by.teachmeskills.springbootproject.repositories.ProductSearchSpecification;
import by.teachmeskills.springbootproject.services.ProductService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    @Override
    public List<ProductDto> getCategoryProducts(String category, PagingParamsDto params) {
        Pageable paging = PageRequest.of(params.getPageNumber(), params.getPageSize(), Sort.by("name").ascending());
        return productRepository.findAllByCategory_Name(category, paging).stream().map(productConverter::toDto).toList();
    }

    @Override
    public ProductDto getProductById(int id) throws NoResourceFoundException {
        return productRepository.findById(id).map(productConverter::toDto).
                orElseThrow(() -> new NoResourceFoundException("Product with id " + id + " not found"));
    }

    @Override
    public List<ProductDto> findProducts(SearchCriteriaDto searchCriteriaDto) {
        if (searchCriteriaDto.getPageNumber() < 0) {
            searchCriteriaDto.setPageNumber(0);
        }
        Pageable paging = PageRequest.of(searchCriteriaDto.getPageNumber(), searchCriteriaDto.getPageSize(), Sort.by("name").ascending());
        Specification<Product> specification = new ProductSearchSpecification(searchCriteriaDto);
        return productRepository.findAll(specification, paging).stream().map(productConverter::toDto).toList();
    }

    @Override
    public CartDto addProductToCart(int id, CartDto cartDto) throws NoResourceFoundException {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new NoResourceFoundException("No product with id " + id + " found"));
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
    public void saveToFile(List<ProductDto> products, HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (Writer writer = new OutputStreamWriter(response.getOutputStream())) {
            StatefulBeanToCsv<ProductDto> beanToCsv = new StatefulBeanToCsvBuilder<ProductDto>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator('~')
                    .build();
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=" + "products.csv");
            products.forEach(p -> p.setId(null));
            beanToCsv.write(products);
        }
    }

    @Override
    @Transactional
    public List<ProductDto> loadFromFile(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<ProductDto> csvToBean = new CsvToBeanBuilder<ProductDto>(reader)
                    .withType(ProductDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator('~')
                    .build();
            List<ProductDto> products = new ArrayList<>();
            List<ProductDto> result = new ArrayList<>();
            csvToBean.forEach(products::add);
            products.stream().map(productConverter::fromDto).forEach(p -> {
                productRepository.save(p);
                result.add(productConverter.toDto(p));
            });
            return result;
        }
    }

    @Override
    @Transactional
    public ProductDto create(ProductDto product) {
        return productConverter.toDto(productRepository.save(productConverter.fromDto(product)));
    }

    @Override
    public List<ProductDto> read(PagingParamsDto params) {
        Pageable paging = PageRequest.of(params.getPageNumber(), params.getPageSize(), Sort.by("name").ascending());
        return productRepository.findAll(paging).stream().map(productConverter::toDto).toList();
    }

    @Override
    @Transactional
    public ProductDto update(ProductDto product) throws NoResourceFoundException {
        productRepository.findById(product.getId()).orElseThrow(() ->
                new NoResourceFoundException("No product with id " + product.getId() + " found"));
        return productConverter.toDto(productRepository.save(productConverter.fromDto(product)));
    }

    @Override
    @Transactional
    public void delete(int id) {
        productRepository.deleteById(id);
    }
}
