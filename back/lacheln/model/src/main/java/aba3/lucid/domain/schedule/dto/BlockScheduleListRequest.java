package aba3.lucid.domain.schedule.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class BlockScheduleListRequest {
    private Long pdId;
    private LocalDate searchDate;
}
