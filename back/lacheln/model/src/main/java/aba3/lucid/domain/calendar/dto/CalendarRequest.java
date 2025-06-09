package aba3.lucid.domain.calendar.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CalendarRequest {

    @NotNull(message = "캘린더 데이터가 필수 입럭값입니다")
    private LocalDate date;

    @Valid
    private CalendarDetailRequest details;

}
