package by.teachmeskills.springbootproject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatisticsDto {
    @NotNull(message = "Не введено")
    int userId;

    @Min(value = 0, message = "Минимум 0")
    @NotNull
    int daysRegistered;

    @Min(value = 0, message = "Минимум 0")
    @NotNull
    int orderCount;

    @Min(value = 0, message = "Минимум 0")
    @NotNull
    int booksCount;

    @Size(min = 10, max = 90, message = "Длина адреса должна быть в пределах от 10 до 90 символов")
    @NotNull
    String favoriteGenre;
}
