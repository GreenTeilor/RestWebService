package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.dto.CategoryDto;
import by.teachmeskills.springbootproject.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("home")
@RequiredArgsConstructor
public class HomeController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories() {
        return categoryService.read();
    }
}

