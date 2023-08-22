package by.teachmeskills.springbootproject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchCriteriaDto {
    @NotNull(message = "Pagination number in searchCriteria is null")
    private Integer paginationNumber;

    @NotNull(message = "KeyWords in searchCriteria is null")
    private String keyWords;
    private String searchCategory;
    private Integer priceFrom;
    private Integer priceTo;
}
