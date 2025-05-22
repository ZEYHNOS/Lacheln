package aba3.lucid.schedule.Controller;

import aba3.lucid.common.api.API;
import aba3.lucid.schedule.Service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/holiday/{companyId}")
    public API<List<LocalDate>> getHolidayList(
            @PathVariable Long companyId
    ) {
        List<LocalDate> holidayList = reservationService.getHolidayList(companyId);

        return API.OK(holidayList);
    }


}
