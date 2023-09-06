package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.constants.SessionAttributesNames;
import by.teachmeskills.springbootproject.dto.ProductDto;
import by.teachmeskills.springbootproject.dto.SearchCriteriaDto;
import by.teachmeskills.springbootproject.services.ProductService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Tag(name = "search", description = "Search endpoints")
@RestController
@RequestMapping("/search")
@SessionAttributes(SessionAttributesNames.SEARCH_CRITERIA)
@RequiredArgsConstructor
@Validated
public class SearchController {
    private final ProductService productService;

    @Operation(
            summary = "Find products by key words",
            description = "Find products by key words in shop")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "All products were found by key word",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
            )
    })
    @PostMapping
    public List<ProductDto> search(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Search criteria object") @Valid @RequestBody SearchCriteriaDto searchCriteriaDto,
                                   BindingResult bindingResult) {
        return productService.findProducts(searchCriteriaDto);
    }
}
