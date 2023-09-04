package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.constraints.NumberConstraint;
import by.teachmeskills.springbootproject.dto.CategoryDto;
import by.teachmeskills.springbootproject.dto.PagingParamsDto;
import by.teachmeskills.springbootproject.dto.ProductDto;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;
import by.teachmeskills.springbootproject.exceptions.UserAlreadyExistsException;
import by.teachmeskills.springbootproject.services.CategoryService;
import by.teachmeskills.springbootproject.services.ProductService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "categories", description = "Category endpoints")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @Operation(
            summary = "Get category products",
            description = "Get products which have certain category name")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "All products were found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
            )
    })
    @GetMapping("/{name}")
    public List<ProductDto> getProducts(@Parameter(description = "Category name") @PathVariable String name,
                                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Paging object") @Valid @RequestBody PagingParamsDto params,
                                        BindingResult bindingResult) {
        return productService.getCategoryProducts(name, params);
    }

    @Operation(
            summary = "Add category",
            description = "Add category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category was added",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class)))
            )
    })
    @PostMapping("/addCategory")
    public List<CategoryDto> add(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Category object") @Valid @RequestBody CategoryDto categoryDto,
                                 BindingResult bindingResult) throws UserAlreadyExistsException {
        categoryService.create(categoryDto);
        return categoryService.read(new PagingParamsDto(0, 100_000_000));
    }

    @Operation(
            summary = "Remove category",
            description = "Remove category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category was removed"
            )
    })
    @DeleteMapping("/removeCategory/{id}")
    public void remove(@Parameter(description = "Category id") @NumberConstraint @PathVariable String id) {
        categoryService.delete(Integer.parseInt(id));
    }

    @Operation(
            summary = "Update category",
            description = "Update category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category was updated",
                    content = @Content(schema = @Schema(implementation = CategoryDto.class))
            )
    })
    @PutMapping("/updateCategory")
    public CategoryDto update(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Category object") @Valid @RequestBody CategoryDto categoryDto, BindingResult bindingResult) throws NoResourceFoundException {
        return categoryService.update(categoryDto);
    }

    @Operation(
            summary = "Save categories to file",
            description = "Save categories to .csv file")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Categories were saved"
            )
    })
    @PostMapping("/csv/export")
    public void exportToCsv(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Category objects") @Valid @RequestBody List<CategoryDto> categories,
                           HttpServletResponse response,
                           BindingResult bindingResult) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        categoryService.saveToFile(categories, response);
    }

    @Operation(
            summary = "Load categories from file",
            description = "Load categories from .csv file and persist in database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Categories were loaded",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class)))
            )
    })
    @PostMapping("/csv/import")
    public List<CategoryDto> importFromCsv(@Parameter(description = "Loaded file") @RequestParam("file") MultipartFile file)
            throws IOException {
        return categoryService.loadFromFile(file);
    }
}
