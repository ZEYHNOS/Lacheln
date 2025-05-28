package aba3.lucid.domain.schedule.dto;

import aba3.lucid.common.enums.Weekdays;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeekdaysScheduleRequest {

    private Long cpId;
    private Weekdays weekday;
    private LocalTime start;
    private LocalTime  end;

}
