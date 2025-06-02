package aba3.lucid.domain.schedule.dto;


import aba3.lucid.common.enums.HolidayWeek;
import aba3.lucid.common.enums.Weekdays;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegularHolidayRequest {
    private Weekdays weekdays;
    private HolidayWeek week;
    private int days;
    private int months;
}
