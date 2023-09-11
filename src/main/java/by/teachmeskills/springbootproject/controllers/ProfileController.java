package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.dto.PagingParamsDto;
import by.teachmeskills.springbootproject.dto.UserDto;
import by.teachmeskills.springbootproject.dto.complex.UserInfoResponse;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;
import by.teachmeskills.springbootproject.services.AuthService;
import by.teachmeskills.springbootproject.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "profile", description = "Profile endpoints")
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@Validated
public class ProfileController {

    private final UserService userService;
    private final AuthService authService;

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
    @GetMapping
    public UserInfoResponse getUserInfo(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Paging object") @Valid @RequestBody PagingParamsDto params,
                                        BindingResult bindingResult) throws NoResourceFoundException {
        return userService.getUserInfo(authService.getPrincipal().map(UserDto::getId).orElse(null), params);
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
    public UserDto addAddressAndPhoneNumberInfo(@Parameter(description = "User address")
                                                @Size(min = 10, max = 90, message = "User address length is less than 10 or more than 90")
                                                @RequestParam("address") String address,
                                                @Parameter(description = "User phoneNumber")
                                                //Doesn't work for some reason
                                                //@Pattern(regexp = "^\\+375((29)|(44)|(25)|(33))[0-9]{7}$", message = "Incorrect user phone number")
                                                @RequestParam("phoneNumber") String phoneNumber)
            throws NoResourceFoundException {
        return userService.addAddressAndPhoneNumberInfo(address, phoneNumber, authService.getPrincipal().orElse(null));
    }

}
