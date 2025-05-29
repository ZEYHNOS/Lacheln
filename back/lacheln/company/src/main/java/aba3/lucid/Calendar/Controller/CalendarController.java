package aba3.lucid.Calendar.Controller;


import aba3.lucid.Calendar.Business.CalendarBusiness;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.dto.CalendarResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/calendar")
@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarBusiness calendarBusiness;

    @PostMapping("/create")
    public API<CalendarResponse> createCalendar(
            @AuthenticationPrincipal CustomUserDetails company,
            @RequestBody CalendarRequest request


    ){
        CalendarResponse response = calendarBusiness.createCalendar(request, company.getCompanyId());
        log.debug("Create Calendar:{}", response);
        return API.OK(response);


    }

    @PutMapping("/update")
    public API<CalendarResponse> updateCalendar(
            @AuthenticationPrincipal CustomUserDetails company,
            @RequestParam Long callId,
            @RequestBody CalendarRequest request
    ) {
       CalendarResponse response = calendarBusiness.updateCalendar(request, company.getCompanyId(), callId);
        return API.OK(response);
    }


}
