package by.teachmeskills.springbootproject.services;

import by.teachmeskills.springbootproject.entities.Cart;
import by.teachmeskills.springbootproject.entities.Statistics;
import by.teachmeskills.springbootproject.entities.User;
import by.teachmeskills.springbootproject.exceptions.AuthorizationException;
import by.teachmeskills.springbootproject.exceptions.InsufficientFundsException;
import by.teachmeskills.springbootproject.exceptions.NoProductsInOrderException;
import by.teachmeskills.springbootproject.exceptions.UserAlreadyExistsException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

public interface UserService extends BaseService<User>{
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserById(int id);
    ModelAndView getUser(String email, String password, BindingResult bindingResult, Model model) throws AuthorizationException;
    void updateAddressAndPhoneNumber(String address, String phoneNumber, String email);
    ModelAndView getUserOrders(User user);
    ModelAndView addAddressAndPhoneNumberInfo(String address, String phoneNumber, User userInSession, BindingResult bindingResult);

    Statistics getUserStatistics(int id);

    ModelAndView makeOrder(User user, Cart cart) throws InsufficientFundsException, UserAlreadyExistsException, NoProductsInOrderException;
}
