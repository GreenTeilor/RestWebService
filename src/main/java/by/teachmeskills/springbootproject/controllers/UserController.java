package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.dto.UserDto;
import by.teachmeskills.springbootproject.exceptions.AuthorizationException;
import by.teachmeskills.springbootproject.exceptions.UserAlreadyExistsException;
import by.teachmeskills.springbootproject.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "user", description = "User endpoints")
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Authenticate user",
            description = "Authenticate user by email and password")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authorized\tNot authorized",
                    content = {@Content(schema = @Schema(implementation = UserDto.class)),
                            @Content(schema = @Schema(implementation = String.class))}
            ),
    })
    @PostMapping("authenticate")
    public UserDto authenticate(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User object") @Valid @RequestBody UserDto user,
                                BindingResult bindingResult) throws AuthorizationException {
        return userService.authorizeUser(user.getEmail(), user.getPassword());
    }

    @Operation(
            summary = "Register user",
            description = "Register user by name, lastName, email, password, birthDate")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User was registered\tUser already exists",
                    content = {@Content(schema = @Schema(implementation = UserDto.class)),
                            @Content(schema = @Schema(implementation = String.class))}
            )
    })
    @PostMapping("register")
    public UserDto register(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User object") @Valid @RequestBody UserDto userDto,
                            BindingResult bindingResult) throws UserAlreadyExistsException {
        return userService.create(userDto);
    }
}
