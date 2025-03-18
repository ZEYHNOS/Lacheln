package aba3.lucid.jwtconfig.repository;

import aba3.lucid.jwtconfig.CompanyRefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRefreshTokenRepository extends CrudRepository<CompanyRefreshToken, Long> {
    Optional<CompanyRefreshToken> findByCpId(Long CompanyId);
    Optional<CompanyRefreshToken> findByRefreshToken(String refreshToken);
}
