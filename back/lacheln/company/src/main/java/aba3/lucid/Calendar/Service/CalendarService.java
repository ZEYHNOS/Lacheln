package aba3.lucid.Calendar.Service;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.calendar.dto.CalendarDetailRequest;
import aba3.lucid.domain.calendar.dto.CalendarDetailResponse;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.calendar.repository.CalendarDetailRepository;
import aba3.lucid.domain.calendar.repository.CalendarRepository;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final CalendarDetailRepository calendarDetailRepository;

    @Transactional
    public CalendarEntity createCalendar(CalendarEntity calendarEntity, CalendarDetailEntity calendarDetailEntity) {
        calendarEntity.getCalendarDetailEntity().add(calendarDetailEntity);
        return calendarRepository.save(calendarEntity);
    }

    // 일 단위로 배열에 저장하고 해당 일의 리스트에 일정 넣기(정렬)  0번째 index 사용하지 않음
    public List<CalendarDetailEntity>[] sortedCalendarSchedule(List<CalendarEntity> calendarEntityList, YearMonth yearMonth) {
        int lastDay = yearMonth.atEndOfMonth().getDayOfMonth();
        List<CalendarDetailEntity>[] result = new List[lastDay+1];
        for (int i = 0; i <= lastDay; i++) {
            result[i] = new ArrayList<>();
        }

        for (CalendarEntity calendar : calendarEntityList) {
            int day = calendar.getCalDate().getDayOfMonth();

            for (CalendarDetailEntity calendarDetail : calendar.getCalendarDetailEntity()) {
                result[day].add(calendarDetail);
            }

            // 정렬
            sortSchedule(result[day]);
        }

        return result;
    }

    public void sortSchedule(List<CalendarDetailEntity> calendarDetailEntityList) {
        calendarDetailEntityList.sort(new Comparator<CalendarDetailEntity>() {
            @Override
            public int compare(CalendarDetailEntity o1, CalendarDetailEntity o2) {
                return o1.getCalDtStart().compareTo(o2.getCalDtStart());
            }
        });
    }

    @Transactional
    public CalendarDetailEntity updateCalendarDetail(CalendarDetailEntity calendar, CalendarDetailRequest request, CompanyEntity company) {
        if (!calendar.getCalendar().getCompany().equals(company)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }
        calendar.updateField(request);
        return calendarDetailRepository.save(calendar);
    }

    public CalendarEntity initCalendar(CompanyEntity company, LocalDate date) {
        Optional<CalendarEntity> optionalCalendar = calendarRepository.findByCompanyAndCalDate(company, date);

        return optionalCalendar.orElseGet(() -> CalendarEntity.builder()
                .company(company)
                .calDate(date)
                .calendarDetailEntity(new ArrayList<>())
                .build());
    }

    // 업체, 년, 월 캘린더 호출
    public List<CalendarEntity> findCompanyCalendarByYearMonth(CompanyEntity company, YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return calendarRepository.findByCompanyAndCalDateBetween(company, startDate, endDate);
    }

    public CalendarDetailEntity findDetailIdWithThrow(Long calDtId) {
        return calendarDetailRepository.findById(calDtId)
                .orElseThrow(() -> new ApiException(ErrorCode.UNAUTHORIZED));
    }

    @Transactional
    public void deleteCalendarDetail(CalendarDetailEntity calendarDetail, CompanyEntity company) {
        if (!calendarDetail.getCalendar().getCompany().equals(company)) {
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }
        calendarDetailRepository.delete(calendarDetail);
    }

    public boolean existsCompanyAndDate(CompanyEntity company, LocalDateTime start) {
    }
}