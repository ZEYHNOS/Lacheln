package aba3.lucid.domain.calendar.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarResponse {

    private LocalDate date;

    List<CalendarDetailResponse> calendarDetailList;

}
