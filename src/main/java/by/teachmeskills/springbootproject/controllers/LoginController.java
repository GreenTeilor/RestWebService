package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.dto.UserDto;
import by.teachmeskills.springbootproject.exceptions.AuthorizationException;
import by.teachmeskills.springbootproject.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
@RequiredArgsConstructor
@Validated
public class LoginController {
    private final UserService userService;

    @PostMapping
    public UserDto login(@Valid @RequestBody UserDto user, BindingResult bindingResult) throws AuthorizationException {
        return userService.authorizeUser(user.getEmail(), user.getPassword());
    }
}
