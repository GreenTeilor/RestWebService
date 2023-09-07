package by.teachmeskills.springbootproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchCriteriaDto extends PagingParamsDto {
    private String keyWords;
    private String searchCategory;
    private Integer priceFrom;
    private Integer priceTo;
}
