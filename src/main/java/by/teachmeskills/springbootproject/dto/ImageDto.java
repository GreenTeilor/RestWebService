package by.teachmeskills.springbootproject.dto;

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
public class ImageDto extends BaseDto {
    private String imagePath;
    private int categoryId;
    private int productId;
    private boolean primary;
}
