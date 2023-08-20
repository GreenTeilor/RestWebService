package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.constants.SessionAttributesNames;
import by.teachmeskills.springbootproject.entities.SearchCriteria;
import by.teachmeskills.springbootproject.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("search")
@SessionAttributes(SessionAttributesNames.SEARCH_CRITERIA)
@RequiredArgsConstructor
public class SearchController {
    private final ProductService productService;

    @GetMapping
    public ModelAndView openSearchPage(@ModelAttribute(SessionAttributesNames.SEARCH_CRITERIA) SearchCriteria searchCriteria) {
        searchCriteria.setKeyWords("");
        searchCriteria.setPaginationNumber(1);
        return productService.findProducts(searchCriteria);
    }

    @PostMapping
    public ModelAndView search(@RequestParam String keyWords, @ModelAttribute(SessionAttributesNames.SEARCH_CRITERIA) SearchCriteria searchCriteria) {
        searchCriteria.setKeyWords(keyWords);
        searchCriteria.setPaginationNumber(1);
        return productService.findProducts(searchCriteria);
    }

    @GetMapping("next")
    public ModelAndView paginationNext(@ModelAttribute(SessionAttributesNames.SEARCH_CRITERIA) SearchCriteria searchCriteria) {
        searchCriteria.setPaginationNumber(searchCriteria.getPaginationNumber() + 1);
        return productService.findProducts(searchCriteria);
    }

    @GetMapping("prev")
    public ModelAndView paginationPrev(@ModelAttribute(SessionAttributesNames.SEARCH_CRITERIA) SearchCriteria searchCriteria) {
        searchCriteria.setPaginationNumber(searchCriteria.getPaginationNumber() - 1);
        return productService.findProducts(searchCriteria);
    }

    @GetMapping("{pageNumber}")
    public ModelAndView paginationPageNumber(@PathVariable int pageNumber, @ModelAttribute(SessionAttributesNames.SEARCH_CRITERIA) SearchCriteria searchCriteria) {
        searchCriteria.setPaginationNumber(pageNumber);
        return productService.findProducts(searchCriteria);
    }

    @ModelAttribute(SessionAttributesNames.SEARCH_CRITERIA)
    public SearchCriteria initializeSearchCriteria() {
        return new SearchCriteria();
    }
}
