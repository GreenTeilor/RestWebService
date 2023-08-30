package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.dto.OrderDto;
import by.teachmeskills.springbootproject.dto.UserDto;
import by.teachmeskills.springbootproject.exceptions.AuthorizationException;
import by.teachmeskills.springbootproject.exceptions.UserAlreadyExistsException;
import by.teachmeskills.springbootproject.services.UserService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "user", description = "User endpoints")
@RestController
@RequestMapping("/user")
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
    @PostMapping("/authenticate")
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
    @PostMapping("/register")
    public UserDto register(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User object") @Valid @RequestBody UserDto userDto,
                            BindingResult bindingResult) throws UserAlreadyExistsException {
        return userService.create(userDto);
    }

    @Operation(
            summary = "Save orders to file",
            description = "Save orders to .csv file")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orders were saved"
            )
    })
    @PostMapping("/saveOrders")
    public void saveOrdersToFile(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Order objects") @Valid @RequestBody List<OrderDto> orders,
                                 BindingResult bindingResult) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        userService.saveOrdersToFile(orders);
    }

    @Operation(
            summary = "Load orders from file",
            description = "Load orders from .csv file and persist in database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orders were loaded",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))
            )
    })
    @PostMapping("/loadOrders")
    public List<OrderDto> loadOrdersFromFile(@Parameter(description = "Loaded file") @RequestParam("file") MultipartFile file)
            throws IOException {
        return userService.loadOrdersFromFile(file);
    }
}
