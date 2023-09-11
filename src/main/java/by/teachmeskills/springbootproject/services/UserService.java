package by.teachmeskills.springbootproject.services;

import by.teachmeskills.springbootproject.dto.CartDto;
import by.teachmeskills.springbootproject.dto.OrderDto;
import by.teachmeskills.springbootproject.dto.PagingParamsDto;
import by.teachmeskills.springbootproject.dto.UserDto;
import by.teachmeskills.springbootproject.dto.complex.UserInfoResponse;
import by.teachmeskills.springbootproject.entities.Statistics;
import by.teachmeskills.springbootproject.exceptions.InsufficientFundsException;
import by.teachmeskills.springbootproject.exceptions.NoProductsInOrderException;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService extends BaseService<UserDto>{
    UserDto getUserById(int id) throws NoResourceFoundException;
    UserInfoResponse getUserInfo(int id, PagingParamsDto params) throws NoResourceFoundException;
    UserDto addAddressAndPhoneNumberInfo(String address, String phoneNumber, UserDto user) throws NoResourceFoundException;
    Statistics getUserStatistics(int id);
    OrderDto makeOrder(UserDto userDto, CartDto cartDto) throws InsufficientFundsException, NoProductsInOrderException;
    void saveOrdersToFile(List<OrderDto> orders, HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;
    List<OrderDto> loadOrdersFromFile(MultipartFile file) throws IOException;
}
