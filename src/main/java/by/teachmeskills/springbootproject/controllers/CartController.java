package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.constants.SessionAttributesNames;
import by.teachmeskills.springbootproject.constraints.NumberConstraint;
import by.teachmeskills.springbootproject.dto.OrderDto;
import by.teachmeskills.springbootproject.dto.CartDto;
import by.teachmeskills.springbootproject.exceptions.InsufficientFundsException;
import by.teachmeskills.springbootproject.exceptions.NoProductsInOrderException;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;
import by.teachmeskills.springbootproject.services.AuthService;
import by.teachmeskills.springbootproject.services.ProductService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

@Tag(name = "cart", description = "Cart endpoints")
@RestController
@RequestMapping("/cart")
@SessionAttributes(SessionAttributesNames.CART)
@RequiredArgsConstructor
@Validated
public class CartController {

    private final ProductService productService;
    private final UserService userService;
    private final AuthService authService;

    @Operation(
            summary = "Add product",
            description = "Add product to cart")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was added to cart",
                    content = @Content(schema = @Schema(implementation = CartDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    @PostMapping("/addProduct/{id}")
    public CartDto addProduct(@Parameter(description = "Product id") @NumberConstraint @PathVariable String id,
                              @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cart object") @Valid @RequestBody CartDto cartDto,
                              BindingResult bindingResult) throws NoResourceFoundException {
        return productService.addProductToCart(Integer.parseInt(id), cartDto);
    }

    @Operation(
            summary = "Remove product",
            description = "Remove product from cart")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was removed from cart",
                    content = @Content(schema = @Schema(implementation = CartDto.class))
            )
    })
    @DeleteMapping("/removeProduct/{id}")
    public CartDto removeProduct(@Parameter(description = "Product id") @NumberConstraint @PathVariable String id,
                                 @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cart object") @Valid @RequestBody CartDto cartDto,
                                 BindingResult bindingResult) {
        return productService.removeProductFromCart(Integer.parseInt(id), cartDto);
    }

    @Operation(
            summary = "Clear cart",
            description = "Remove all products from cart")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products were removed from cart",
                    content = @Content(schema = @Schema(implementation = CartDto.class))
            )
    })
    @DeleteMapping("/clear")
    public CartDto clear(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cart object") @Valid @RequestBody CartDto cartDto,
                         BindingResult bindingResult) {
        return productService.removeAllProductsFromCart(cartDto);
    }

    @Operation(
            summary = "Make order",
            description = "Make order based on what cart contains")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order was made\tInsufficient funds\tNo products in cart",
                    content = {@Content(schema = @Schema(implementation = OrderDto.class)),
                            @Content(schema = @Schema(implementation = String.class))}
            ),
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/makeOrder")
    public OrderDto makeOrder(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request object with cart and user") @Valid @RequestBody CartDto cartDto,
                              BindingResult bindingResult) throws InsufficientFundsException, NoProductsInOrderException {
        return userService.makeOrder(authService.getPrincipal().orElse(null), cartDto);
    }
}
