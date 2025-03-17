package aba3.lucid.jwtconfig.repository;

import aba3.lucid.jwtconfig.CompanyRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRefreshTokenRepository extends JpaRepository<CompanyRefreshToken, Long> {
    Optional<CompanyRefreshToken> findByUserId(Long userId);
    Optional<CompanyRefreshToken> findByRefreshToken(String refreshToken);
}
