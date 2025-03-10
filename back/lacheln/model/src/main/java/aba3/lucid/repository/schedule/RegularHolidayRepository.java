package aba3.lucid.repository.schedule;

import aba3.lucid.domain.schedule.RegularHolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegularHolidayRepository extends JpaRepository<RegularHolidayEntity, Long> {

}
