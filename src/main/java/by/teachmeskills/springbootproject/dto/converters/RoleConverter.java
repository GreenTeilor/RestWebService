package by.teachmeskills.springbootproject.dto.converters;

import by.teachmeskills.springbootproject.dto.RoleDto;
import by.teachmeskills.springbootproject.entities.Role;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleConverter implements Converter<RoleDto, Role> {
    @Override
    public RoleDto toDto(Role role) {
        return Optional.ofNullable(role).map(r -> RoleDto.builder().
                        id(r.getId()).
                        name(r.getName()).
                        build()).
                orElse(null);
    }

    @Override
    public Role fromDto(RoleDto roleDto) {
        return Optional.ofNullable(roleDto).map(r -> Role.builder().
                        id(r.getId()).
                        name(r.getName()).
                        build()).
                orElse(null);
    }
}
