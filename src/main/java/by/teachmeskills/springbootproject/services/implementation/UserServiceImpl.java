package by.teachmeskills.springbootproject.services.implementation;

import by.teachmeskills.springbootproject.constants.PagesPaths;
import by.teachmeskills.springbootproject.constants.RequestAttributesNames;
import by.teachmeskills.springbootproject.entities.Cart;
import by.teachmeskills.springbootproject.entities.Order;
import by.teachmeskills.springbootproject.entities.Statistics;
import by.teachmeskills.springbootproject.entities.User;
import by.teachmeskills.springbootproject.exceptions.AuthorizationException;
import by.teachmeskills.springbootproject.exceptions.InsufficientFundsException;
import by.teachmeskills.springbootproject.exceptions.NoProductsInOrderException;
import by.teachmeskills.springbootproject.exceptions.UserAlreadyExistsException;
import by.teachmeskills.springbootproject.repositories.UserRepository;
import by.teachmeskills.springbootproject.services.UserService;
import by.teachmeskills.springbootproject.utils.ErrorPopulatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.getUserById(id);
    }

    @Override
    public ModelAndView getUser(String email, String password, BindingResult bindingResult, Model model) throws AuthorizationException {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.LOGIN_PAGE);
        if (!bindingResult.hasFieldErrors(RequestAttributesNames.EMAIL) && !bindingResult.hasFieldErrors(RequestAttributesNames.PASSWORD)) {
            Optional<User> authenticatedUser = userRepository.getUser(email, password);
            if (authenticatedUser.isPresent()) {
                model.addAttribute(RequestAttributesNames.USER, authenticatedUser.get());
                return new ModelAndView("redirect:" + PagesPaths.HOME_PAGE);
            }
            throw new AuthorizationException("Неверный логин или пароль");
        }
        ErrorPopulatorUtils.populateError(RequestAttributesNames.EMAIL, modelAndView, bindingResult);
        ErrorPopulatorUtils.populateError(RequestAttributesNames.PASSWORD, modelAndView, bindingResult);
        return modelAndView;
    }

    @Override
    @Transactional
    public void updateAddressAndPhoneNumber(String address, String phoneNumber, String email) {
        userRepository.updateAddressAndPhoneNumber(address, phoneNumber, email);
    }

    public static ModelAndView makeModelAndView(User user, Statistics statistics, List<Order> orders) {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.PROFILE_PAGE);
        modelAndView.addObject(user);
        modelAndView.addObject(statistics);
        modelAndView.addObject(RequestAttributesNames.ORDERS, orders);
        return modelAndView;
    }

    @Override
    public ModelAndView getUserOrders(User user) {
        Statistics statistics = getUserStatistics(user.getId());
        List<Order> orders = user.getOrders();
        return makeModelAndView(user, statistics, orders);
    }

    @Override
    @Transactional
    public ModelAndView addAddressAndPhoneNumberInfo(String address, String phoneNumber, User user, BindingResult bindingResult) {
        Statistics statistics = getUserStatistics(user.getId());
        List<Order> orders = user.getOrders();
        if (!bindingResult.hasFieldErrors(RequestAttributesNames.ADDRESS) && !bindingResult.hasFieldErrors(RequestAttributesNames.PHONE_NUMBER)) {
            user.setAddress(address);
            user.setPhoneNumber(phoneNumber);
            userRepository.updateAddressAndPhoneNumber(address, phoneNumber, user.getEmail());
        }
        ModelAndView modelAndView = makeModelAndView(user, statistics, orders);
        ErrorPopulatorUtils.populateError(RequestAttributesNames.ADDRESS, modelAndView, bindingResult);
        ErrorPopulatorUtils.populateError(RequestAttributesNames.PHONE_NUMBER, modelAndView, bindingResult);
        return modelAndView;
    }

    @Override
    public Statistics getUserStatistics(int id) {
        return Statistics.builder().userId(id).daysRegistered(userRepository.getUserDaysRegistered(id)).
                orderCount(userRepository.getUserOrdersCount(id)).
                booksCount(userRepository.getUserPurchasedBooksCount(id)).
                favoriteGenre(userRepository.getUserFavoriteGenre(id)).build();
    }

    @Override
    @Transactional
    public ModelAndView makeOrder(User user, Cart cart) throws InsufficientFundsException, NoProductsInOrderException {
        BigDecimal orderPrice = cart.getPrice();
        if (user.getBalance().compareTo(orderPrice) < 0) {
            throw new InsufficientFundsException("Недостаточно средств");
        }
        if (cart.isEmpty()) {
            throw new NoProductsInOrderException("Корзина пуста");
        }
        Order order = Order.builder().userId(user.getId()).date(LocalDate.now()).products(new ArrayList<>(cart.getProducts())).price(orderPrice).build();
        user.getOrders().add(order);
        user.setBalance(user.getBalance().subtract(orderPrice));
        userRepository.update(user);
        cart.clear();
        ModelAndView modelAndView = new ModelAndView(PagesPaths.CART_PAGE);
        modelAndView.addObject(RequestAttributesNames.STATUS, "Заказ оформлен!");
        modelAndView.addObject(RequestAttributesNames.COLOR, "green");
        return modelAndView;
    }

    @Override
    @Transactional
    public ModelAndView create(User user) throws UserAlreadyExistsException {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.REGISTER_PAGE);
        user.setBalance(BigDecimal.valueOf(0.0));
        user.setRegistrationDate(LocalDate.now());
        if (getUserByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Такой пользователь уже существует");
        }
        userRepository.create(user);
        modelAndView.addObject(RequestAttributesNames.STATUS, "Успешно");
        modelAndView.addObject(RequestAttributesNames.COLOR, "green");
        return modelAndView;
    }

    @Override
    public ModelAndView read() {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.HOME_PAGE);
        modelAndView.addObject(RequestAttributesNames.USER, userRepository.read());
        return modelAndView;
    }

    @Override
    @Transactional
    public User update(User user) {
        return userRepository.update(user);
    }

    @Override
    @Transactional
    public void delete(int id) {
        userRepository.delete(id);
    }
}
