package aba3.lucid.domain.schedule.repository;

import aba3.lucid.domain.schedule.entity.TemporaryHolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TemporaryHolidayRepository  extends JpaRepository<TemporaryHolidayEntity, Long> {

    Optional<TemporaryHolidayEntity> findByTemHolidayId(Long temHoliday);

    List<TemporaryHolidayEntity> findAllByCompany_CpId(Long companyId);

    boolean existsByCompany_CpIdAndThDate(Long companyId, LocalDate date);
}
