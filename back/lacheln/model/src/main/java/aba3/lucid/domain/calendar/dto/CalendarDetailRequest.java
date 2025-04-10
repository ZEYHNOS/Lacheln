package aba3.lucid.domain.calendar.dto;


import aba3.lucid.common.enums.Color;
import jakarta.validation.constraints.NotBlank;
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
public class CalendarDetailRequest {
    @NotBlank(message = "title 필수 입력 값입니다")
    private String title;

    @NotBlank(message = "내용 필수 입력 값입니다")
    private  String content;

    @NotBlank(message = "시작 시간이 필수 입력값입니다")
    LocalDateTime startTime;

    @NotBlank(message = "마감 시간이 필수 입력값입나다")
    LocalDateTime endTime;

    private Color color = Color.BLUE;

    private String memo;

}
