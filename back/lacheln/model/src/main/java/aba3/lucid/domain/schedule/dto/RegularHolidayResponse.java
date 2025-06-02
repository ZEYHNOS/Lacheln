package aba3.lucid.domain.schedule.dto;


import aba3.lucid.common.enums.HolidayWeek;
import aba3.lucid.common.enums.Weekdays;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegularHolidayResponse {
    private long id;
    private long cpId;
    private Weekdays weekdays;
    private HolidayWeek week;
    private int days;
    private int month;
}
