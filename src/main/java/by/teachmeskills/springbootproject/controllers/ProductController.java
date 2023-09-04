package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.constraints.NumberConstraint;
import by.teachmeskills.springbootproject.dto.PagingParamsDto;
import by.teachmeskills.springbootproject.dto.ProductDto;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;
import by.teachmeskills.springbootproject.exceptions.UserAlreadyExistsException;
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

@Tag(name = "products", description = "Product endpoints")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;

    @Operation(
            summary = "Get product",
            description = "Get product by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was obtained",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))
            )
    })
    @GetMapping("/{id}")
    public ProductDto get(@Parameter(description = "Product id") @NumberConstraint @PathVariable String id) throws NoResourceFoundException {
        return productService.getProductById(Integer.parseInt(id));
    }

    @Operation(
            summary = "Remove product",
            description = "Remove product")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was removed"
            )
    })
    @DeleteMapping("/removeProduct/{id}")
    public void remove(@Parameter(description = "Product id") @NumberConstraint @PathVariable String id) {
        productService.delete(Integer.parseInt(id));
    }

    @Operation(
            summary = "Update product",
            description = "Update product")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was updated",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))
            )
    })
    @PutMapping("/updateProduct")
    public ProductDto update(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product object") @Valid @RequestBody ProductDto productDto,
                             BindingResult bindingResult) throws NoResourceFoundException {
        return productService.update(productDto);
    }

    @Operation(
            summary = "Add product",
            description = "Add product")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was added",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
            )
    })
    @PostMapping("/createProduct")
    public List<ProductDto> add(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product object") @Valid @RequestBody ProductDto productDto,
                                BindingResult bindingResult) throws UserAlreadyExistsException {
        productService.create(productDto);
        return productService.read(new PagingParamsDto(0, 100_000_000));
    }

    @Operation(
            summary = "Save products to file",
            description = "Save products to .csv file")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products were saved"
            )
    })
    @PostMapping("/csv/export")
    public void exportToCsv(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product objects") @Valid @RequestBody List<ProductDto> products,
                           HttpServletResponse response,
                           BindingResult bindingResult) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        productService.saveToFile(products, response);
    }

    @Operation(
            summary = "Load products from file",
            description = "Load products from .csv file and persist in database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products were loaded",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
            )
    })
    @PostMapping ("/csv/import")
    public List<ProductDto> importFromCsv(@Parameter(description = "Loaded file") @RequestParam("file") MultipartFile file)
            throws IOException {
        return productService.loadFromFile(file);
    }
}
