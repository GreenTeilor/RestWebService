package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.constants.SessionAttributesNames;
import by.teachmeskills.springbootproject.constraints.NumberConstraint;
import by.teachmeskills.springbootproject.dto.ProductDto;
import by.teachmeskills.springbootproject.dto.SearchCriteriaDto;
import by.teachmeskills.springbootproject.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@RestController
@RequestMapping("search")
@SessionAttributes(SessionAttributesNames.SEARCH_CRITERIA)
@RequiredArgsConstructor
@Validated
public class SearchController {
    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts() {
        SearchCriteriaDto searchCriteriaDto = new SearchCriteriaDto();
        searchCriteriaDto.setKeyWords("");
        searchCriteriaDto.setPaginationNumber(1);
        return productService.findProducts(searchCriteriaDto);
    }

    @PostMapping
    public List<ProductDto> search(@Valid @RequestBody SearchCriteriaDto searchCriteriaDto, BindingResult bindingResult) {
        searchCriteriaDto.setPaginationNumber(1);
        return productService.findProducts(searchCriteriaDto);
    }

    @GetMapping("next")
    public List<ProductDto> findPagedNext(@Valid @RequestBody SearchCriteriaDto searchCriteriaDto, BindingResult bindingResult) {
        searchCriteriaDto.setPaginationNumber(searchCriteriaDto.getPaginationNumber() + 1);
        return productService.findProducts(searchCriteriaDto);
    }

    @GetMapping("prev")
    public List<ProductDto> findPagedPrev(@Valid @RequestBody SearchCriteriaDto searchCriteriaDto, BindingResult bindingResult) {
        searchCriteriaDto.setPaginationNumber(searchCriteriaDto.getPaginationNumber() - 1);
        return productService.findProducts(searchCriteriaDto);
    }

    @GetMapping("{pageNumber}")
    public List<ProductDto> findPagedNumber(@NumberConstraint @PathVariable String pageNumber,
                                             @Valid @RequestBody SearchCriteriaDto searchCriteriaDto, BindingResult bindingResult) {
        searchCriteriaDto.setPaginationNumber(Integer.parseInt(pageNumber));
        return productService.findProducts(searchCriteriaDto);
    }
}
