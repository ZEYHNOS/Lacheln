package aba3.lucid.domain.calendar.dto;

import aba3.lucid.domain.product.dto.option.OptionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarScheduleRequest {

    private String title;

    private String content;

    private LocalDateTime start;

    private LocalDateTime end;

    private String memo;

    private List<OptionDto> optionDtoList;

    private String managerName;

    private String phoneNum;

}
