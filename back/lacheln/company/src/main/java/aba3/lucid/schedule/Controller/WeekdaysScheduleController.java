package aba3.lucid.schedule.Controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.schedule.dto.BusinessHourResponse;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleRequest;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleResponse;
import aba3.lucid.schedule.Business.WeekdaysScheduleBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/company")
@RequiredArgsConstructor
public class WeekdaysScheduleController {

    private final WeekdaysScheduleBusiness weekdaysScheduleBusiness;

    @PostMapping("/weekdays_schedule")
    public API<WeekdaysScheduleResponse>createSchedule(
           @AuthenticationPrincipal CustomUserDetails company,
            @RequestBody WeekdaysScheduleRequest request
    ) {
        WeekdaysScheduleResponse response = weekdaysScheduleBusiness.createSchedule(request, company);
        return API.OK(response);
    }


    @PutMapping("/update/weekdays_schedule")
    public API<WeekdaysScheduleResponse>updateSchedule(
              @RequestParam Long wsId,
              @RequestBody WeekdaysScheduleRequest request
    ){
      WeekdaysScheduleResponse response = weekdaysScheduleBusiness.updateSchedule(request,wsId);
      return API.OK(response);
    }

    @GetMapping("/business_hour/{companyId}")
    public API<BusinessHourResponse[]> getBusinessHour(
            @PathVariable Long companyId
    ) {
        BusinessHourResponse[] response = weekdaysScheduleBusiness.getBusinessHourArray(companyId);

        return API.OK(response);
    }

    //프론트엔드에서 사용자가 스케줄을 생성하거나 변경할 때 이 엔드포인트에 GET 요청을 보내서 기본 폼을 채울 수 있습니다
//
//    @GetMapping("/weekdays-schedule/default")
//    public API<List<WeekdaysScheduleRequest>> getDefaultSchedules() {
//        return API.OK(weekdaysScheduleBusiness.getDefaultSchedules());
//    }


}
