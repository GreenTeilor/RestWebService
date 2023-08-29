package by.teachmeskills.springbootproject.dto.converters;

import by.teachmeskills.springbootproject.dto.StatisticsDto;
import by.teachmeskills.springbootproject.entities.Statistics;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StatisticsConverter {

    public StatisticsDto toDto(Statistics statistics) {
        return Optional.ofNullable(statistics).map(s -> StatisticsDto.builder().
                        userId(s.getUserId()).
                        daysRegistered(s.getDaysRegistered()).
                        orderCount(s.getOrderCount()).
                        booksCount(s.getBooksCount()).
                        favoriteGenre(s.getFavoriteGenre()).
                        build()).
                orElse(null);
    }

    public Statistics fromDto(StatisticsDto statisticsDto) {
        return Optional.ofNullable(statisticsDto).map(s -> Statistics.builder().
                        userId(s.getUserId()).
                        daysRegistered(s.getDaysRegistered()).
                        orderCount(s.getOrderCount()).
                        booksCount(s.getBooksCount()).
                        favoriteGenre(s.getFavoriteGenre()).
                        build()).
                orElse(null);
    }
}
