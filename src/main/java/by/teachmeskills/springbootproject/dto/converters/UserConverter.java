package by.teachmeskills.springbootproject.dto.converters;

import by.teachmeskills.springbootproject.dto.UserDto;
import by.teachmeskills.springbootproject.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserConverter implements Converter<UserDto, User> {

    private final OrderConverter orderConverter;
    private final RoleConverter roleConverter;

    @Override
    public UserDto toDto(User user) {
        return Optional.ofNullable(user).map(u -> UserDto.builder().
                        id(u.getId()).
                        name(u.getName()).
                        lastName(u.getLastName()).
                        email(u.getEmail()).
                        birthDate(u.getBirthDate()).
                        registrationDate(u.getRegistrationDate()).
                        balance(u.getBalance()).
                        password(u.getPassword()).
                        address(u.getAddress()).
                        phoneNumber(u.getPhoneNumber()).
                        orders(Optional.ofNullable(u.getOrders()).map(orders -> orders.stream().map(orderConverter::toDto).toList()).orElse(List.of())).
                        roles(Optional.ofNullable(u.getRoles()).map(roles -> roles.stream().map(roleConverter::toDto).toList()).orElse(List.of())).
                        build()).
                orElse(null);
    }

    @Override
    public User fromDto(UserDto userDto) {
        return Optional.ofNullable(userDto).map(u -> User.builder().
                        id(u.getId()).
                        name(u.getName()).
                        lastName(u.getLastName()).
                        email(u.getEmail()).
                        birthDate(u.getBirthDate()).
                        registrationDate(u.getRegistrationDate()).
                        balance(u.getBalance()).
                        password(u.getPassword()).
                        address(u.getAddress()).
                        phoneNumber(u.getPhoneNumber()).
                        orders(Optional.ofNullable(u.getOrders()).map(orders -> orders.stream().map(orderConverter::fromDto).toList()).orElse(List.of())).
                        roles(Optional.ofNullable(u.getRoles()).map(roles -> roles.stream().map(roleConverter::fromDto).toList()).orElse(List.of())).
                        build()).
                orElse(null);
    }
}
