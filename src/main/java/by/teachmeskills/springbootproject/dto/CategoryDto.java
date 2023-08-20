package by.teachmeskills.springbootproject.dto;

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
    private String name;
    private String imagePath;
    private List<ProductDto> products;
}
