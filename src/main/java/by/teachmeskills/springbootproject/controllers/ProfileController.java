package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.constraints.NumberConstraint;
import by.teachmeskills.springbootproject.dto.UserDto;
import by.teachmeskills.springbootproject.dto.complex.UserInfoResponse;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;
import by.teachmeskills.springbootproject.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "profile", description = "Profile endpoints")
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@Validated
public class ProfileController {

    private final UserService userService;

    @Operation(
            summary = "Get user info",
            description = "Get user info by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User info was obtained",
                    content = @Content(schema = @Schema(implementation = UserInfoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User was not found",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    @GetMapping("/{id}")
    public UserInfoResponse getUserInfo(@Parameter(description = "User id") @NumberConstraint @PathVariable String id)
            throws NoResourceFoundException {
        return userService.getUserInfo(Integer.parseInt(id));
    }

    @Operation(
            summary = "Add info",
            description = "Add user address and phone number info")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Address and phone number info were added",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            )
    })
    @PostMapping
    public UserDto addAddressAndPhoneNumberInfo(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User object") @Valid @RequestBody UserDto userDto,
                                                BindingResult bindingResult) {
        return userService.addAddressAndPhoneNumberInfo(userDto.getAddress(), userDto.getPhoneNumber(), userDto);
    }

}
