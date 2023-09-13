package by.teachmeskills.springbootproject.repositories;

import by.teachmeskills.springbootproject.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByUsername(String username);
}
