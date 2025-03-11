package aba3.lucid.repository.product;

import aba3.lucid.domain.product.DressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DressRepository extends JpaRepository<DressEntity, Long> {

    List<DressEntity> findAllByProduct_Company_cpId(long id);

}
