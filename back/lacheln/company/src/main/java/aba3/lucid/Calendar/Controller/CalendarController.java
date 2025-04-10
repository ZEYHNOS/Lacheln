package aba3.lucid.Calendar.Controller;


import aba3.lucid.Calendar.Business.CalendarBusiness;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.dto.CalendarResponse;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
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

    @PostMapping("{companyId}/create")
    public API<CalendarResponse> createCalendar(
            @RequestBody CalendarRequest request

    ){
        CalendarResponse response = calendarBusiness.createCalendar(request, AuthUtil.getCompanyId());
        log.debug("Create Calendar:{}", response);
        return API.OK();


    }

    @PutMapping("{companyId}/{calId}")
    public API<CalendarResponse> updateCalendar(
            @PathVariable Long calId,
            @RequestBody CalendarRequest request
    ) {
        log.debug("Update Calendar:{}", calId);
        CalendarResponse response = calendarBusiness.updateCalendar(request, calId, AuthUtil.getCompanyId());
        return API.OK();
    }


}
