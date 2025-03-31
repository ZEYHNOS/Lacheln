package aba3.lucid.schedule.Controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleRequest;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleResponse;
import aba3.lucid.schedule.Service.WeekdaysScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/company")
@RequiredArgsConstructor
public class WeekdaysScheduleController {
    private final WeekdaysScheduleService weekdaysScheduleService;

    @PostMapping("/{cpId}/weekdays-schedule")
    public ResponseEntity<?> createSchedule(
            @PathVariable Long cpId,
            @RequestBody WeekdaysScheduleRequest request
    ) {
        weekdaysScheduleService.saveSchedules(cpId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{cpId}/weekdays-schedule")
    public ResponseEntity<List<WeekdaysScheduleResponse>> getSchedules(
            @PathVariable Long cpId
    ){
        List<WeekdaysScheduleResponse> response = weekdaysScheduleService.getSchedules(cpId);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/update/{cpId}/weekdays-schedule")
    public API<List<WeekdaysScheduleResponse>> updateSchedule(
            @PathVariable Long cpId,
            @RequestBody WeekdaysScheduleRequest request
    ){
        List<WeekdaysScheduleResponse> response  = weekdaysScheduleService.updateCompanySchedules(cpId, request);
        return API.OK(response);
    }


}
