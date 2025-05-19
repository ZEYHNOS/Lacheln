package aba3.lucid.Calendar.Controller;


import aba3.lucid.Calendar.Business.CalendarBusiness;
import aba3.lucid.Calendar.Service.CalendarService;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.dto.CalendarResponse;
import aba3.lucid.domain.calendar.dto.CalendarUpdateRequest;
import aba3.lucid.domain.calendar.dto.CalendarUpdateResponse;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.calendar.repository.CalendarRepository;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/calendar")
@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarBusiness calendarBusiness;
    private final CalendarRepository calendarRepository;
    private final CalendarService calendarService;

    @PostMapping("/create")
    public API<CalendarResponse> createCalendar(
            @RequestBody CalendarRequest request

    ){
        CalendarResponse response = calendarBusiness.createCalendar(request, AuthUtil.getCompanyId());
        log.debug("Create Calendar:{}", response);
        return API.OK();


    }

    @PutMapping("/update")
    public API<CalendarResponse> updateCalendar(
            @PathVariable Long calId,
            @RequestBody CalendarUpdateRequest request
    ) {
        CalendarUpdateResponse response = calendarBusiness.updateCalendar(request, AuthUtil.getCompanyId(), calId);
        return API.OK();
    }


}
