package aba3.lucid.domain.schedule.repository;

import aba3.lucid.domain.schedule.entity.TemporaryHolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemporaryHolidayRepository  extends JpaRepository<TemporaryHolidayEntity, Long> {

}
