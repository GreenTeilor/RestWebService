package by.teachmeskills.springbootproject.services;

import by.teachmeskills.springbootproject.dto.OrderDto;
import by.teachmeskills.springbootproject.dto.UserDto;
import by.teachmeskills.springbootproject.dto.complex.MakeOrderRequestDto;
import by.teachmeskills.springbootproject.dto.complex.UserInfoResponse;
import by.teachmeskills.springbootproject.entities.Statistics;
import by.teachmeskills.springbootproject.exceptions.AuthorizationException;
import by.teachmeskills.springbootproject.exceptions.InsufficientFundsException;
import by.teachmeskills.springbootproject.exceptions.NoProductsInOrderException;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;

public interface UserService extends BaseService<UserDto>{
    UserDto getUserById(int id) throws NoResourceFoundException;
    UserDto authorizeUser(String email, String password) throws AuthorizationException;
    UserInfoResponse getUserInfo(int id) throws NoResourceFoundException;
    UserDto addAddressAndPhoneNumberInfo(String address, String phoneNumber, UserDto user);
    Statistics getUserStatistics(int id);
    OrderDto makeOrder(MakeOrderRequestDto requestDto) throws InsufficientFundsException, NoProductsInOrderException;
}
