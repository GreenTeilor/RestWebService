package by.teachmeskills.springbootproject.repositories;

import by.teachmeskills.springbootproject.entities.BaseEntity;
import by.teachmeskills.springbootproject.exceptions.UserAlreadyExistsException;

import java.util.List;

public interface BaseRepository<T extends BaseEntity> {

    T create(T entity) throws UserAlreadyExistsException;

    List<T> read();

    T update(T entity);

    void delete(int id);
}
