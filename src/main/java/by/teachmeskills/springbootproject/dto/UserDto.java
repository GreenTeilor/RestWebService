package by.teachmeskills.springbootproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto extends BaseDto {
    private String name;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private LocalDate registrationDate;
    private BigDecimal balance;
    private String password;
    private String address;
    private String phoneNumber;
    private List<OrderDto> orders;
}
