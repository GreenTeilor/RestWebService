package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.constants.PagesPaths;
import by.teachmeskills.springbootproject.constants.RequestAttributesNames;
import by.teachmeskills.springbootproject.constants.SessionAttributesNames;
import by.teachmeskills.springbootproject.entities.User;
import by.teachmeskills.springbootproject.exceptions.AuthorizationException;
import by.teachmeskills.springbootproject.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("login")
@SessionAttributes(SessionAttributesNames.USER)
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    @GetMapping
    public ModelAndView openLoginPage() {
        return new ModelAndView(PagesPaths.LOGIN_PAGE);
    }

    @PostMapping
    public ModelAndView login(@Valid @ModelAttribute(RequestAttributesNames.USER) User user, BindingResult bindingResult, Model model) throws AuthorizationException {
        return userService.getUser(user.getEmail(), user.getPassword(), bindingResult, model);
    }

    @ModelAttribute(RequestAttributesNames.USER)
    public User initializeUserInSession() {
        return new User();
    }
}
