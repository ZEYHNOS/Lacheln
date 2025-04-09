package aba3.lucid.domain.calendar.dto;


import aba3.lucid.common.enums.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CalendarDetailResponse {
    private long calDtId;
    private String title;
    private String content;
    private LocalTime starTime;
    private LocalTime endTime;
    private Color color;
    private String manager;
    private String memo;

}
