package aba3.lucid.domain.calendar.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarUpdateRequest implements CalendarRequestIfs {
    @NotBlank
    private long cpId;

    @NotBlank(message = "캘린더 데이터가 필수 입럭값입니다")
    private LocalDate date;

    @NotBlank(message = "EventDetail 필수 입력값입니다")
    private List<CalendarDetailRequest> details;

}
