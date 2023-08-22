package by.teachmeskills.springbootproject.dto.complex;

import by.teachmeskills.springbootproject.dto.StatisticsDto;
import by.teachmeskills.springbootproject.dto.UserDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInfoResponse {
    @NotNull(message = "UserDto is null")
    @Valid
    private UserDto userDto;

    @NotNull(message = "StatisticsDto is null")
    @Valid
    private StatisticsDto statisticsDto;
}
