package aba3.lucid.domain.schedule.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeekdaysScheduleRequest {
    private List<DayScheduleDto> scheduleList;

    //여러 요일 정보를 한 번에 처리하기 위해
    //List<DayScheduleDto>를 두고, 그 안에 요일/시작시간/종료시간을 담는 DayScheduleDto 클래스를 뒀어요
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DayScheduleDto {
        private String weekday;
        private String start;
        private String end;
    }


}
