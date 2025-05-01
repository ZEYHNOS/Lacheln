package aba3.lucid.domain.schedule.dto;


import aba3.lucid.common.enums.Weekdays;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeekdaysScheduleResponse {
    private long wsId;
    private Weekdays weekday;
    private LocalTime start;
    private LocalTime  end;
}
