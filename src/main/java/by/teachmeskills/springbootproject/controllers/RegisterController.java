package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.dto.UserDto;
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
@RequestMapping("register")
@RequiredArgsConstructor
@Validated
public class RegisterController {
    private final UserService service;

    @PostMapping
    public UserDto registerUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) throws UserAlreadyExistsException {
        return service.create(userDto);
    }
}
