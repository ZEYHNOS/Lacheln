package aba3.lucid.repository.schedule;

import aba3.lucid.domain.schedule.TemporaryHolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemporaryHolidayRepository  extends JpaRepository<TemporaryHolidayEntity, Long> {

}
