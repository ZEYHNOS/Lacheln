package aba3.lucid.schedule.Service;


import aba3.lucid.domain.schedule.entity.TemporaryHolidayEntity;
import aba3.lucid.domain.schedule.repository.TemporaryHolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TemporaryHolidayService {

    private final TemporaryHolidayRepository temporaryHolidayRepository;

    public TemporaryHolidayEntity createTemporaryHoliday(TemporaryHolidayEntity temEntity) {
        return  temporaryHolidayRepository.save(temEntity);
    }
}
