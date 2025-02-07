package aba3.lucid.option.repository;

import aba3.lucid.option.model.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository <Long,OptionEntity > {
}
