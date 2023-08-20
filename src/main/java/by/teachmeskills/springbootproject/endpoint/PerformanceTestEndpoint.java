package by.teachmeskills.springbootproject.endpoint;

import by.teachmeskills.springbootproject.repositories.CategoryRepository;
import by.teachmeskills.springbootproject.repositories.ProductRepository;
import by.teachmeskills.springbootproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.ModelAndView;

@Component
@Endpoint(id = "performanceTest")
@RequiredArgsConstructor
public class PerformanceTestEndpoint {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @ReadOperation
    public ModelAndView getResults() {
        ModelAndView modelAndView = new ModelAndView("performanceInfo");
        ModelMap modelMap = new ModelMap();
        long lastResult;
        StopWatch watch = new StopWatch();

        watch.start();
        categoryRepository.read();
        watch.stop();
        lastResult = watch.getTotalTimeNanos();
        modelMap.addAttribute("categoriesReadResult", lastResult);

        watch = new StopWatch();
        watch.start();
        productRepository.read();
        watch.stop();
        lastResult = watch.getTotalTimeNanos();
        modelMap.addAttribute("productsReadResult", lastResult);

        watch = new StopWatch();
        watch.start();
        productRepository.getProductById(2);
        watch.stop();
        lastResult = watch.getTotalTimeNanos();
        modelMap.addAttribute("productFindResult", lastResult);

        watch = new StopWatch();
        watch.start();
        productRepository.findProducts("Последнее желание", 1);
        watch.stop();
        lastResult = watch.getTotalTimeNanos();
        modelMap.addAttribute("productsFindResult", lastResult);

        watch = new StopWatch();
        watch.start();
        userRepository.getUserById(3);
        watch.stop();
        lastResult = watch.getTotalTimeNanos();
        modelMap.addAttribute("userFindResult", lastResult);

        modelAndView.addAllObjects(modelMap);
        return modelAndView;
    }
}
