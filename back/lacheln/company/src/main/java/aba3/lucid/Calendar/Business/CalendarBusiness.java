package aba3.lucid.Calendar.Business;


import aba3.lucid.Calendar.Service.CalendarService;
import aba3.lucid.common.annotation.Business;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.calendar.converter.CalendarConverter;
import aba3.lucid.domain.calendar.converter.CalendarDetailConverter;
import aba3.lucid.domain.calendar.dto.CalendarDetailResponse;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.dto.CalendarResponse;
import aba3.lucid.domain.calendar.entity.CalendarDetailEntity;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class CalendarBusiness {

    private final CompanyService companyService;
    private final CalendarService calendarService;

    private final CalendarConverter calendarConverter;
    private final CalendarDetailConverter calendarDetailConverter;

    @Transactional(readOnly = true)
    public List<CalendarDetailResponse>[] readAllByCpIdAndDate(Long companyId, Integer year, Integer month) {
        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        List<CalendarEntity> calendarList = calendarService.findCompanyCalendarByYearMonth(company, YearMonth.of(year, month));

        int lastDay = YearMonth.of(year, month).atEndOfMonth().getDayOfMonth();
        List<CalendarDetailEntity>[] response = new List[lastDay+1];
        for (int i = 0; i <= lastDay; i++) {
            response[i] = new ArrayList<>();
        }

        for (CalendarEntity calendar : calendarList) {
            int day = calendar.getCalDate().getDayOfMonth();

            for (CalendarDetailEntity calendarDetail : calendar.getCalendarDetailEntity()) {
                response[day].add(calendarDetail);
            }

            response[day].sort(new Comparator<CalendarDetailEntity>() {
                @Override
                public int compare(CalendarDetailEntity o1, CalendarDetailEntity o2) {
                    return o1.getCalDtStart().compareTo(o2.getCalDtStart());
                }
            });
        }

        return calendarConverter.toResponseList(response);
    }



    public CalendarResponse createCalendar(Long companyId, CalendarRequest request) {
        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        CalendarEntity calendar = calendarService.initCalendar(company, request);
        CalendarDetailEntity calendarDetail = calendarDetailConverter.toEntity(request.getDetails(), calendar);

        CalendarEntity savedCalendar = calendarService.createCalendar(calendar, calendarDetail);
        return calendarConverter.toResponse(savedCalendar);
    }
}
