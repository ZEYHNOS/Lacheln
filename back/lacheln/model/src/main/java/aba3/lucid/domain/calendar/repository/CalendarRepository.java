package aba3.lucid.domain.calendar.repository;


import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {

    Optional<CalendarEntity> findByCompanyAndCalDate(CompanyEntity company, LocalDate calDate);

    List<CalendarEntity> findAllByCompany_CpId(Long companyId);

    Optional<CalendarEntity> findByCompany_CpIdAndCalDate(Long companyId, LocalDate date);
}
