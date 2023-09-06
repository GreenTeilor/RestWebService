package by.teachmeskills.springbootproject.services.implementation;

import by.teachmeskills.springbootproject.dto.OrderDto;
import by.teachmeskills.springbootproject.dto.OrderProductDto;
import by.teachmeskills.springbootproject.dto.PagingParamsDto;
import by.teachmeskills.springbootproject.dto.StatisticsDto;
import by.teachmeskills.springbootproject.dto.UserDto;
import by.teachmeskills.springbootproject.dto.complex.MakeOrderRequestDto;
import by.teachmeskills.springbootproject.dto.complex.UserInfoResponse;
import by.teachmeskills.springbootproject.dto.converters.OrderConverter;
import by.teachmeskills.springbootproject.dto.converters.OrdersProductsConverter;
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
import by.teachmeskills.springbootproject.repositories.OrderRepository;
import by.teachmeskills.springbootproject.repositories.UserRepository;
import by.teachmeskills.springbootproject.services.UserService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
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
    private final OrdersProductsConverter ordersProductsConverter;
    private final OrderRepository orderRepository;

    @Override
    public UserDto getUserById(int id) throws NoResourceFoundException {
        return userRepository.findById(id).map(userConverter::toDto).orElseThrow(() ->
                new NoResourceFoundException("No user with id " + id + " found"));
    }

    @Override
    public UserDto authorizeUser(String email, String password) throws AuthorizationException {
        return userRepository.findByEmailAndPassword(email, password).map(userConverter::toDto).orElseThrow(() ->
                new AuthorizationException("User is not authenticated"));
    }

    @Override
    public UserInfoResponse getUserInfo(int id, PagingParamsDto params) throws NoResourceFoundException {
        Pageable paging = PageRequest.of(params.getPageNumber(), params.getPageSize(), Sort.by("date").descending());
        List<Order> pagedOrders = orderRepository.findAllByUserId(id, paging);
        UserDto userDto = getUserById(id);
        userDto.setOrders(pagedOrders.stream().map(orderConverter::toDto).toList());
        StatisticsDto statisticsDto = statisticsConverter.toDto(getUserStatistics(id));
        return new UserInfoResponse(userDto, statisticsDto);
    }

    @Override
    @Transactional
    public UserDto addAddressAndPhoneNumberInfo(String address, String phoneNumber, UserDto user) throws NoResourceFoundException {
        userRepository.findById(user.getId()).orElseThrow(() ->
                new NoResourceFoundException("No user with id " + user.getId() + " found"));
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        userRepository.save(userConverter.fromDto(user));
        return user;
    }

    @Override
    public Statistics getUserStatistics(int id) {
        return Statistics.builder().userId(id).daysRegistered(userRepository.getUserDaysRegistered(id)).
                orderCount(userRepository.getUserOrdersCount(id)).
                booksCount(userRepository.getUserPurchasedBooksCount(id)).
                favoriteGenre(userRepository.getUserFavoriteCategory(id)).build();
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
        User user = userRepository.save(userConverter.fromDto(userDto));
        cartDto.clear();
        List<Order> orders = user.getOrders();
        return orderConverter.toDto(orders.get(orders.size() - 1));
    }

    @Override
    public void saveOrdersToFile(List<OrderDto> orders, HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (Writer writer = new OutputStreamWriter(response.getOutputStream())) {
            StatefulBeanToCsv<OrderProductDto> beanToCsv = new StatefulBeanToCsvBuilder<OrderProductDto>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator('~')
                    .build();
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=" + "orders_products.csv");
            List<OrderProductDto> productCsvs = ordersProductsConverter.fromOrdersDto(orders);
            beanToCsv.write(productCsvs);
        }
    }

    @Override
    @Transactional
    public List<OrderDto> loadOrdersFromFile(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<OrderProductDto> csvToBean = new CsvToBeanBuilder<OrderProductDto>(reader)
                    .withType(OrderProductDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator('~')
                    .build();
            List<OrderProductDto> orderProductDtos = new ArrayList<>();
            List<OrderDto> result = new ArrayList<>();
            csvToBean.forEach(orderProductDtos::add);
            List<OrderDto> orders = ordersProductsConverter.toOrdersDto(orderProductDtos);
            orders.stream().map(orderConverter::fromDto).forEach(o -> {
                orderRepository.save(o);
                result.add(orderConverter.toDto(o));
            });
            return result;
        }
    }

    @Override
    @Transactional
    public UserDto create(UserDto user) throws UserAlreadyExistsException {
        user.setBalance(BigDecimal.valueOf(0.0));
        user.setRegistrationDate(LocalDate.now());
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Такой пользователь уже существует");
        }
        return userConverter.toDto(userRepository.save(userConverter.fromDto(user)));
    }

    @Override
    public List<UserDto> read(PagingParamsDto params) {
        Pageable paging = PageRequest.of(params.getPageNumber(), params.getPageSize(), Sort.by("name").ascending());
        return userRepository.findAll(paging).stream().map(userConverter::toDto).toList();
    }

    @Override
    @Transactional
    public UserDto update(UserDto user) throws NoResourceFoundException {
        userRepository.findById(user.getId()).orElseThrow(() ->
                new NoResourceFoundException("No user with id " + user.getId() + " found"));
        return userConverter.toDto(userRepository.save(userConverter.fromDto(user)));
    }

    @Override
    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
