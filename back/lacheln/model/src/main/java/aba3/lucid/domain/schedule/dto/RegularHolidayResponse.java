package aba3.lucid.domain.schedule.dto;


import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegularHolidayResponse {
    private long id;
    private long cpId;
    private String weekdays;
    private String week;
    private int days;
    private int month;
}
