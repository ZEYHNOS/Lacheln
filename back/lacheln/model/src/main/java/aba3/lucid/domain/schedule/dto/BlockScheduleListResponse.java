package aba3.lucid.domain.schedule.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BlockScheduleListResponse {
    private List<BlockScheduleDto> blockSchedules;
}
