package by.teachmeskills.springbootproject.services;

import by.teachmeskills.springbootproject.entities.BaseEntity;
import by.teachmeskills.springbootproject.exceptions.UserAlreadyExistsException;
import org.springframework.web.servlet.ModelAndView;

public interface BaseService<T extends BaseEntity> {
    ModelAndView create(T entity) throws UserAlreadyExistsException;

    ModelAndView read();

    T update(T entity);

    void delete(int id);
}
