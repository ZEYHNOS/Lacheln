package aba3.lucid.jwtconfig.repository;

import aba3.lucid.jwtconfig.CompanyRefreshToken;
import aba3.lucid.jwtconfig.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRefreshTokenRepository extends JpaRepository<CompanyRefreshToken, Long> {
    Optional<UserRefreshToken> findByUserId(Long userId);
    Optional<UserRefreshToken> findByRefreshToken(String refreshToken);
}
