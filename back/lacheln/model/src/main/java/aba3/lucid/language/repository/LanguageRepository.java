package aba3.lucid.language.repository;

import aba3.lucid.language.model.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<String, LanguageEntity> {

}
