package aba3.lucid.domain.schedule.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeekdaysScheduleResponse {
    private long wsId;
    private String weekday;
    private String start;
    private String end;
}
