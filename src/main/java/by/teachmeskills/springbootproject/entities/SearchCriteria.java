package by.teachmeskills.springbootproject.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchCriteria {
    private Integer paginationNumber;
    private String keyWords;
    private String searchCategory;
    private Integer priceFrom;
    private Integer priceTo;
}
