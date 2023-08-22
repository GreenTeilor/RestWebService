package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.constraints.IntConstraint;
import by.teachmeskills.springbootproject.dto.CategoryDto;
import by.teachmeskills.springbootproject.dto.ProductDto;
import by.teachmeskills.springbootproject.exceptions.UserAlreadyExistsException;
import by.teachmeskills.springbootproject.services.CategoryService;
import by.teachmeskills.springbootproject.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("{name}")
    public List<ProductDto> getCategoryProducts(@PathVariable String name) {
        return productService.getCategoryProducts(name);
    }

    @PostMapping("addCategory")
    public List<CategoryDto> addCategory(@Valid @RequestBody CategoryDto categoryDto, BindingResult bindingResult) throws UserAlreadyExistsException {
        categoryService.create(categoryDto);
        return categoryService.read();
    }

    @DeleteMapping("removeCategory/{id}")
    public void removeCategory(@IntConstraint @PathVariable String id) {
        categoryService.delete(Integer.parseInt(id));
    }

    @PutMapping("updateCategory")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto, BindingResult bindingResult) {
        return categoryService.update(categoryDto);
    }
}
