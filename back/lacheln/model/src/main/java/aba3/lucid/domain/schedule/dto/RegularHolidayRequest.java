package aba3.lucid.domain.schedule.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegularHolidayRequest {
    private String weekdays;
    private String week;
    private int days;
    private int months;
}
