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
    @NotNull(message = "Empty userId")
    int userId;

    @Min(value = 0, message = "Мin 0")
    @NotNull(message = "Empty daysRegistered")
    int daysRegistered;

    @Min(value = 0, message = "Мin 0")
    @NotNull(message = "Empty orderCount")
    int orderCount;

    @Min(value = 0, message = "Min 0")
    @NotNull(message = "Empty booksCount")
    int booksCount;

    @Size(min = 1, max = 50, message = "Category name is empty or longer than 50 chars")
    @NotNull(message = "Empty favoriteGenre")
    String favoriteGenre;
}
