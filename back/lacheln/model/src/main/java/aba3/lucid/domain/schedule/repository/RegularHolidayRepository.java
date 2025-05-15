package aba3.lucid.domain.schedule.repository;

import aba3.lucid.domain.company.entity.AdjustmentEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.schedule.entity.RegularHolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegularHolidayRepository extends JpaRepository<RegularHolidayEntity, Long> {
    List<RegularHolidayEntity> findAllByCompany_CpId(Long cpId);
    Optional<RegularHolidayEntity> findByRegHolidayId(Long regHolidayId);

}
