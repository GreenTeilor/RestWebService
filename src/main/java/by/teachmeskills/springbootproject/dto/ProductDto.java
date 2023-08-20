package by.teachmeskills.springbootproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDto extends BaseDto{
    private String name;
    private String description;
    private String imagePath;
    private String categoryName;
    private BigDecimal price;
}
