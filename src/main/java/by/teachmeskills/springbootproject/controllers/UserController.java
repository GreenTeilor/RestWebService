package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.dto.UserDto;
import by.teachmeskills.springbootproject.exceptions.AuthorizationException;
import by.teachmeskills.springbootproject.exceptions.UserAlreadyExistsException;
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
@RequestMapping("user")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping("authenticate")
    public UserDto authenticate(@Valid @RequestBody UserDto user, BindingResult bindingResult) throws AuthorizationException {
        return userService.authorizeUser(user.getEmail(), user.getPassword());
    }

    @PostMapping("register")
    public UserDto register(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) throws UserAlreadyExistsException {
        return userService.create(userDto);
    }
}
