package br.com.aiclasstracker.classtracker.Repository;

import br.com.aiclasstracker.classtracker.Entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByEmail(String email);
    Optional<TokenEntity> findByToken(String token);
}