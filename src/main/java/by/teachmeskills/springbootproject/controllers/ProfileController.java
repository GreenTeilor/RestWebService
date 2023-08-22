package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.constraints.IntConstraint;
import by.teachmeskills.springbootproject.dto.UserDto;
import by.teachmeskills.springbootproject.dto.complex.UserInfoResponse;
import by.teachmeskills.springbootproject.entities.User;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;
import by.teachmeskills.springbootproject.services.UserService;
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


@RestController
@RequestMapping("profile")
@RequiredArgsConstructor
@Validated
public class ProfileController {

    private final UserService userService;

    @GetMapping("{id}")
    public UserInfoResponse getUserInfo(@IntConstraint @PathVariable String id) throws NoResourceFoundException {
        return userService.getUserInfo(Integer.parseInt(id));
    }

    @PostMapping
    public UserDto addAddressAndPhoneNumberInfo(@Valid @RequestBody User user, BindingResult bindingResult) {
        return userService.addAddressAndPhoneNumberInfo(user.getAddress(), user.getPhoneNumber(), user);
    }

}
