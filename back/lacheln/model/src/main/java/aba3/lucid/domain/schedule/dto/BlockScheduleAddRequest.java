package aba3.lucid.domain.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BlockScheduleAddRequest {

    @NotBlank
    private Long pdId;

    @NotBlank
    private LocalDateTime startTime;

    @NotBlank
    private Integer taskTime;
}
