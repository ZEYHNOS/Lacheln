package aba3.lucid.domain.schedule.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BlockScheduleDeleteRequest {
    private Long pdId;
    private LocalDateTime startTime;
    private Integer taskTime;
}