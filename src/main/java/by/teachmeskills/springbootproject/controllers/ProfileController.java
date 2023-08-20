package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.constants.SessionAttributesNames;
import by.teachmeskills.springbootproject.entities.User;
import by.teachmeskills.springbootproject.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public ModelAndView openProfilePage(@SessionAttribute(SessionAttributesNames.USER) User user) {
        return userService.getUserOrders(user);
    }

    @PostMapping
    public ModelAndView addAddressAndPhoneNumberInfo(@Valid @ModelAttribute(SessionAttributesNames.USER) User user, BindingResult bindingResult, @SessionAttribute(SessionAttributesNames.USER) User userInSession) {
        return userService.addAddressAndPhoneNumberInfo(user.getAddress(), user.getPhoneNumber(), userInSession, bindingResult);
    }

}
