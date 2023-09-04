package by.teachmeskills.springbootproject.endpoint;

import by.teachmeskills.springbootproject.dto.SearchCriteriaDto;
import by.teachmeskills.springbootproject.entities.Product;
import by.teachmeskills.springbootproject.repositories.CategoryRepository;
import by.teachmeskills.springbootproject.repositories.ProductRepository;
import by.teachmeskills.springbootproject.repositories.ProductSearchSpecification;
import by.teachmeskills.springbootproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Endpoint(id = "performanceTest")
@RequiredArgsConstructor
public class PerformanceTestEndpoint {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @ReadOperation
    public Map<String, Long> getResults() {
        Map<String, Long> result = new LinkedHashMap<>();
        long lastResult;
        StopWatch watch = new StopWatch();

        watch.start();
        categoryRepository.findAll();
        watch.stop();
        lastResult = watch.getTotalTimeNanos();
        result.put("categoriesReadResult", lastResult);

        watch = new StopWatch();
        watch.start();
        productRepository.findAll();
        watch.stop();
        lastResult = watch.getTotalTimeNanos();
        result.put("productsReadResult", lastResult);

        watch = new StopWatch();
        watch.start();
        productRepository.findById(2);
        watch.stop();
        lastResult = watch.getTotalTimeNanos();
        result.put("productFindResult", lastResult);

        watch = new StopWatch();
        watch.start();
        SearchCriteriaDto searchCriteriaDto = new SearchCriteriaDto();
        searchCriteriaDto.setKeyWords("Последнее желание");
        Specification<Product> specification = new ProductSearchSpecification(searchCriteriaDto);
        productRepository.findAll(specification);
        watch.stop();
        lastResult = watch.getTotalTimeNanos();
        result.put("ProductsFindResult", lastResult);

        watch = new StopWatch();
        watch.start();
        userRepository.findById(3);
        watch.stop();
        lastResult = watch.getTotalTimeNanos();
        result.put("userFindResult", lastResult);

        return result;
    }
}
