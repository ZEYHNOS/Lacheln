package aba3.lucid.domain.country.repository;

import aba3.lucid.domain.country.entity.CountryEntity;
import aba3.lucid.domain.user.enums.CountryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, CountryEnum> {
}
