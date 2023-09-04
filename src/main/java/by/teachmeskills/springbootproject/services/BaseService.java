package by.teachmeskills.springbootproject.services;

import by.teachmeskills.springbootproject.dto.BaseDto;
import by.teachmeskills.springbootproject.dto.PagingParamsDto;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;
import by.teachmeskills.springbootproject.exceptions.UserAlreadyExistsException;

import java.util.List;

public interface BaseService<T extends BaseDto> {
    T create(T entity) throws UserAlreadyExistsException;

    List<T> read(PagingParamsDto params);

    T update(T entity) throws NoResourceFoundException;

    void delete(int id);
}
