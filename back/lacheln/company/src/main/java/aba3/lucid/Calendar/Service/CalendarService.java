package aba3.lucid.Calendar.Service;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.calendar.repository.CalendarRepository;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    @Transactional
    public CalendarEntity createCalendar(CalendarEntity calendarEntity, CalendarDetailEntity calendarDetailEntity) {
        calendarEntity.getCalendarDetailEntity().add(calendarDetailEntity);
        return calendarRepository.save(calendarEntity);
    }


    public CalendarEntity findById(Long calId) {
        return calendarRepository.findById(calId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "존재하지 않는 캘린더입니다." + calId));
    }

    @Transactional
    public CalendarEntity updateCalendar(CalendarEntity calendar) {
        return calendarRepository.save(calendar);
    }

    public List<CalendarEntity> findAllByCpId(Long companyId) {
        return calendarRepository.findAllByCompany_CpId(companyId);
    }

    public CalendarEntity initCalendar(CompanyEntity company, CalendarRequest request) {
        Optional<CalendarEntity> optionalCalendar = calendarRepository.findByCompanyAndCalDate(company, request.getDate());

        return optionalCalendar.orElseGet(() -> CalendarEntity.builder()
                .company(company)
                .calDate(request.getDate())
                .calendarDetailEntity(new ArrayList<>())
                .build());
    }

    public CalendarEntity findByCpIdAndDateWithThrow(Long companyId, LocalDate date) {
        return calendarRepository.findByCompany_CpIdAndCalDate(companyId, date)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }
}