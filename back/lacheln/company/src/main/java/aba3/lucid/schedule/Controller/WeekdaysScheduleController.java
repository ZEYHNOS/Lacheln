package aba3.lucid.schedule.Controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleRequest;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleResponse;
import aba3.lucid.schedule.Business.WeekdaysScheduleBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/company")
@RequiredArgsConstructor
public class WeekdaysScheduleController {
    private final WeekdaysScheduleBusiness weekdaysScheduleBusiness;

    @PostMapping("/{cpId}/weekdays-schedule")
    public API<List<WeekdaysScheduleResponse>>createSchedule(
            @PathVariable Long cpId,
            @RequestBody WeekdaysScheduleRequest request
    ) {
        List<WeekdaysScheduleResponse> response = weekdaysScheduleBusiness.createSchedule(request, AuthUtil.getCompanyId());
        return API.OK(response);
    }


      @PutMapping("/update/{cpId}/weekdays-schedule")
      public API<List<WeekdaysScheduleResponse>>updateSchedule(
              @PathVariable Long cpId,
              @RequestParam Long wsId,
              @RequestBody WeekdaysScheduleRequest request
      ){
        List<WeekdaysScheduleResponse> response = weekdaysScheduleBusiness.updateSchedule(request,AuthUtil.getCompanyId(),wsId);
        return API.OK(response);
      }


}
