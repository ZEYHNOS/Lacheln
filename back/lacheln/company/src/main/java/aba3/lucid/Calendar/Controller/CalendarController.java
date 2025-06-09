package aba3.lucid.Calendar.Controller;


import aba3.lucid.Calendar.Business.CalendarBusiness;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.calendar.dto.CalendarDetailResponse;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.dto.CalendarResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@Slf4j
@RequestMapping("/calendar")
@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarBusiness calendarBusiness;

    @GetMapping("/{year}/{month}")
    public API<List<CalendarDetailResponse>[]> readDateSchedule(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Integer year,
            @PathVariable Integer month
    ) {
        List<CalendarDetailResponse>[] responseList = calendarBusiness.readAllByCpIdAndDate(user.getCompanyId(), year, month);
        return API.OK(responseList);
    }

    @PostMapping("/create")
    public API<CalendarResponse> create(
            @RequestBody CalendarRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        CalendarResponse response = calendarBusiness.createCalendar(user.getCompanyId(), request);
        return API.OK(response);
    }
}
