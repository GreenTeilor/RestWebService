package by.teachmeskills.springbootproject.dto;


import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderProductDto extends ProductDto {
    @CsvBindByName
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate orderDate;

    @CsvBindByName
    private int userId;

    @CsvBindByName
    private BigDecimal price;

    @CsvBindByName
    private Integer orderId;
}
