package by.teachmeskills.springbootproject.services.implementation;

import by.teachmeskills.springbootproject.dto.OrderDto;
import by.teachmeskills.springbootproject.dto.StatisticsDto;
import by.teachmeskills.springbootproject.dto.UserDto;
import by.teachmeskills.springbootproject.dto.complex.MakeOrderRequestDto;
import by.teachmeskills.springbootproject.dto.complex.UserInfoResponse;
import by.teachmeskills.springbootproject.dto.converters.OrderConverter;
import by.teachmeskills.springbootproject.dto.converters.ProductConverter;
import by.teachmeskills.springbootproject.dto.converters.StatisticsConverter;
import by.teachmeskills.springbootproject.dto.converters.UserConverter;
import by.teachmeskills.springbootproject.dto.CartDto;
import by.teachmeskills.springbootproject.entities.Order;
import by.teachmeskills.springbootproject.entities.Statistics;
import by.teachmeskills.springbootproject.entities.User;
import by.teachmeskills.springbootproject.exceptions.AuthorizationException;
import by.teachmeskills.springbootproject.exceptions.InsufficientFundsException;
import by.teachmeskills.springbootproject.exceptions.NoProductsInOrderException;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;
import by.teachmeskills.springbootproject.exceptions.UserAlreadyExistsException;
import by.teachmeskills.springbootproject.repositories.UserRepository;
import by.teachmeskills.springbootproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final OrderConverter orderConverter;
    private final ProductConverter productConverter;
    private final StatisticsConverter statisticsConverter;

    @Override
    public UserDto getUserById(int id) throws NoResourceFoundException {
        return userRepository.getUserById(id).map(userConverter::toDto).orElseThrow(() -> new NoResourceFoundException("No user with id " + id + " found"));
    }

    @Override
    public UserDto authorizeUser(String email, String password) throws AuthorizationException {
        return userRepository.getUser(email, password).map(userConverter::toDto).orElseThrow(() -> new AuthorizationException("User is not authenticated"));
    }

    @Override
    public UserInfoResponse getUserInfo(int id) throws NoResourceFoundException {
        UserDto userDto = getUserById(id);
        StatisticsDto statisticsDto = statisticsConverter.toDto(getUserStatistics(id));
        return new UserInfoResponse(userDto, statisticsDto);
    }

    @Override
    @Transactional
    public UserDto addAddressAndPhoneNumberInfo(String address, String phoneNumber, UserDto user) {
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        userRepository.updateAddressAndPhoneNumber(address, phoneNumber, user.getEmail());
        return user;
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
    public OrderDto makeOrder(MakeOrderRequestDto requestDto) throws InsufficientFundsException, NoProductsInOrderException {
        UserDto userDto = requestDto.getUserDto();
        CartDto cartDto = requestDto.getCartDto();
        BigDecimal orderPrice = cartDto.getPrice();
        if (userDto.getBalance().compareTo(orderPrice) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        if (cartDto.isEmpty()) {
            throw new NoProductsInOrderException("Cart is empty");
        }
        Order order = Order.builder().userId(userDto.getId()).date(LocalDate.now()).products(
                new ArrayList<>(cartDto.getProducts().stream().map(productConverter::fromDto).toList())).price(orderPrice).build();
        OrderDto orderDto = orderConverter.toDto(order);
        userDto.getOrders().add(orderDto);
        userDto.setBalance(userDto.getBalance().subtract(orderPrice));
        User user = userRepository.update(userConverter.fromDto(userDto));
        cartDto.clear();
        List<Order> orders = user.getOrders();
        return orderConverter.toDto(orders.get(orders.size() - 1));
    }

    @Override
    @Transactional
    public UserDto create(UserDto user) throws UserAlreadyExistsException {
        user.setBalance(BigDecimal.valueOf(0.0));
        user.setRegistrationDate(LocalDate.now());
        if (userRepository.getUserByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Такой пользователь уже существует");
        }
        return userConverter.toDto(userRepository.create(userConverter.fromDto(user)));
    }

    @Override
    public List<UserDto> read() {
        return userRepository.read().stream().map(userConverter::toDto).toList();
    }

    @Override
    @Transactional
    public UserDto update(UserDto user) {
        return userConverter.toDto(userRepository.update(userConverter.fromDto(user)));
    }

    @Override
    @Transactional
    public void delete(int id) {
        userRepository.delete(id);
    }
}
