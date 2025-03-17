package aba3.lucid.jwtconfig.repository;

import aba3.lucid.jwtconfig.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    Optional<UserRefreshToken> findByUserId(String userId);
    Optional<UserRefreshToken> findByRefreshToken(String refreshToken);
}
