package aba3.lucid.domain.calendar.dto;


import aba3.lucid.common.enums.Color;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarDetailRequest {

    @NotBlank(message = "title 필수 입력 값입니다")
    private String title;

    @NotBlank(message = "내용 필수 입력 값입니다")
    private  String content;

    @NotNull(message = "시작 시간이 필수 입력값입니다")
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Color color;

    private String memo;

    private String manager;

}
