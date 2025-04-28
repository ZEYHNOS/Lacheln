package aba3.lucid.domain.calendar.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarRequest implements CalendarRequestIfs {
    @NotBlank(message = "캘린더 데이터가 필수 입럭값입니다")
    private LocalDate date;


    @NotBlank(message = "EventDetail 필수 입력값입니다")
    private List<CalendarDetailRequest> details;

}
