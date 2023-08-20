package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.constants.RequestAttributesNames;
import by.teachmeskills.springbootproject.entities.User;
import by.teachmeskills.springbootproject.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("home")
@RequiredArgsConstructor
public class HomeController {
    private final CategoryService categoryService;

    @GetMapping
    public ModelAndView openHomePage(@SessionAttribute(RequestAttributesNames.USER) User user) {
        ModelAndView modelAndView = categoryService.read();
        modelAndView.addObject(RequestAttributesNames.USER, user);
        return modelAndView;
    }
}

