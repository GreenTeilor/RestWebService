package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.dto.CategoryDto;
import by.teachmeskills.springbootproject.dto.PagingParamsDto;
import by.teachmeskills.springbootproject.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "home", description = "Home endpoints")
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
@Validated
public class HomeController {
    private final CategoryService categoryService;

    @Operation(
            summary = "Find all categories",
            description = "Find all existing categories in shop")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "All categories were found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class)))
            )
    })
    @GetMapping
    public List<CategoryDto> getCategories(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Paging object") @Valid @RequestBody PagingParamsDto params,
                                           BindingResult bindingResult) {
        return categoryService.read(params);
    }
}

