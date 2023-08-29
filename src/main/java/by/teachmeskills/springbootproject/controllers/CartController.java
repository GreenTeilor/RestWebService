package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.constants.SessionAttributesNames;
import by.teachmeskills.springbootproject.constraints.NumberConstraint;
import by.teachmeskills.springbootproject.dto.OrderDto;
import by.teachmeskills.springbootproject.dto.CartDto;
import by.teachmeskills.springbootproject.dto.complex.MakeOrderRequestDto;
import by.teachmeskills.springbootproject.exceptions.InsufficientFundsException;
import by.teachmeskills.springbootproject.exceptions.NoProductsInOrderException;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;
import by.teachmeskills.springbootproject.services.ProductService;
import by.teachmeskills.springbootproject.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

@RestController
@RequestMapping("cart")
@SessionAttributes(SessionAttributesNames.CART)
@RequiredArgsConstructor
@Validated
public class CartController {

    private final ProductService productService;
    private final UserService userService;

    @PostMapping("addProduct/{id}")
    public CartDto addProduct(@NumberConstraint @PathVariable String id, @Valid @RequestBody CartDto cartDto, BindingResult bindingResult) throws
            NoResourceFoundException {
        return productService.addProductToCart(Integer.parseInt(id), cartDto);
    }

    @DeleteMapping("removeProduct/{id}")
    public CartDto removeProduct(@NumberConstraint @PathVariable String id, @Valid @RequestBody CartDto cartDto, BindingResult bindingResult) {
        return productService.removeProductFromCart(Integer.parseInt(id), cartDto);
    }

    @DeleteMapping("clear")
    public CartDto clear(@Valid @RequestBody CartDto cartDto, BindingResult bindingResult) {
        return productService.removeAllProductsFromCart(cartDto);
    }

    @PostMapping("makeOrder")
    public OrderDto makeOrder(@Valid @RequestBody MakeOrderRequestDto requestDto, BindingResult bindingResult) throws
            InsufficientFundsException, NoProductsInOrderException {
        return userService.makeOrder(requestDto);
    }
}
