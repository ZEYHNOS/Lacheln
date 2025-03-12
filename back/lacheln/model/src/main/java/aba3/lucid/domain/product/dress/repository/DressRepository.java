package aba3.lucid.domain.product.dress.repository;

import aba3.lucid.domain.product.dress.entity.DressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DressRepository extends JpaRepository<DressEntity, Long> {

    List<DressEntity> findAllByProduct_Company_cpId(long id);

}
