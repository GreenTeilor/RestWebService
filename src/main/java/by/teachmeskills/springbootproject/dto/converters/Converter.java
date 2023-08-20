package by.teachmeskills.springbootproject.dto.converters;

import by.teachmeskills.springbootproject.dto.BaseDto;
import by.teachmeskills.springbootproject.entities.BaseEntity;

public interface Converter <T extends BaseDto, E extends BaseEntity>{
    T toDto(E entityObject);
    E fromDto(T dtoObject);
}
