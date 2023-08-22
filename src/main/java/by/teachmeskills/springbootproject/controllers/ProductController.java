package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.constraints.IntConstraint;
import by.teachmeskills.springbootproject.dto.ProductDto;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;
import by.teachmeskills.springbootproject.exceptions.UserAlreadyExistsException;
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
@RequestMapping("products")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService service;

    @GetMapping("{id}")
    public ProductDto getProduct(@IntConstraint @PathVariable String id) throws NoResourceFoundException {
        return service.getProductById(Integer.parseInt(id));
    }

    @DeleteMapping("removeProduct/{id}")
    public void removeProduct(@IntConstraint @PathVariable String id) {
        service.delete(Integer.parseInt(id));
    }

    @PutMapping("updateProduct")
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto, BindingResult bindingResult) {
        return service.update(productDto);
    }

    @PostMapping("createProduct")
    public List<ProductDto> addProduct(@Valid @RequestBody ProductDto productDto, BindingResult bindingResult) throws UserAlreadyExistsException {
        service.create(productDto);
        return service.read();
    }
}
