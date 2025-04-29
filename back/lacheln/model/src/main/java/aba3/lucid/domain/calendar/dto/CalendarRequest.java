package aba3.lucid.domain.calendar.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CalendarRequest implements CalendarRequestIfs {
    @NotBlank(message = "캘린더 데이터가 필수 입럭값입니다")
    private LocalDate date;


    @NotBlank(message = "EventDetail 필수 입력값입니다")
    private List<CalendarDetailRequest> details;

}
