package aba3.lucid.domain.schedule.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.schedule.dto.BlockScheduleDto;
import aba3.lucid.domain.schedule.entity.BlockScheduleEntity;

import java.util.List;

@Converter
public class BlockScheduleConvertor {
    
    // Dto로 변환
    public List<BlockScheduleDto> convertToDto(List<BlockScheduleEntity> scheduleEntityList) {
        return scheduleEntityList.stream()
                .map(scheduleEntity -> BlockScheduleDto
                        .builder()
                        .endTime(scheduleEntity.getEndTime())
                        .pdId(scheduleEntity.getPdId())
                        .startTime(scheduleEntity.getStartTime())
                        .build())
                .toList();
    }

    public BlockScheduleEntity convertToEntity(BlockScheduleDto scheduleDto) {
        return BlockScheduleEntity.builder()
                .pdId(scheduleDto.getPdId())
                .startTime(scheduleDto.getStartTime())
                .endTime(scheduleDto.getEndTime())
                .build();
    }
}
