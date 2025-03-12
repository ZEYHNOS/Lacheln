package aba3.lucid.domain.schedule.repository;

import aba3.lucid.domain.schedule.entity.RegularHolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegularHolidayRepository extends JpaRepository<RegularHolidayEntity, Long> {

}
