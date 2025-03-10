package aba3.lucid.repository.user;

import aba3.lucid.domain.user.CountryEntity;
import aba3.lucid.domain.user.enums.CountryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, CountryEnum> {
}
