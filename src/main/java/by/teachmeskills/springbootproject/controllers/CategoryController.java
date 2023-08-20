package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.constants.RequestAttributesNames;
import by.teachmeskills.springbootproject.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ProductService service;

    @GetMapping("{name}")
    public ModelAndView openCategory(@PathVariable String name) {
        ModelAndView modelAndView = service.getCategoryProducts(name);
        modelAndView.addObject(RequestAttributesNames.CATEGORY_NAME, name);
        return modelAndView;
    }
}
