package by.teachmeskills.springbootproject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDto extends BaseDto {

    @Size(min = 1, max = 50, message = "Category name is empty or longer than 50 chars")
    @NotNull(message = "Empty category name")
    private String name;

    @Size(min = 1, max = 50, message = "Category name is empty or longer than 50 chars")
    @NotNull(message = "Empty category imagePath")
    private String imagePath;

    @NotNull(message = "List of category products is empty")
    private List<ProductDto> products;
}
