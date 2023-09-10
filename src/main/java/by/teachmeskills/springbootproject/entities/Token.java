package by.teachmeskills.springbootproject.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class Token extends BaseEntity {
    @NotNull
    private String token;

    @NotNull
    private String username;
}
