package aba3.lucid.domain.product.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.product.dto.option.OptionDto;
import aba3.lucid.domain.product.entity.OptionDetailEntity;
import aba3.lucid.domain.product.entity.OptionEntity;

import java.util.List;

@Converter
public class OptionDetailConverter {

    // 옵션 상세 dto -> entity
    public OptionDetailEntity toEntity(OptionDto.OptionDetailDto dto, OptionEntity entity) {
        if (dto == null) {
            return null;
        }

        return OptionDetailEntity.builder()
                .option(entity)
                .opDtName(dto.getOpDtName().trim())
                .opDtPlusCost(dto.getPlusCost())
                .opDtPlusTime(dto.getPlusTime())
                .build()
                ;
    }

    public List<OptionDetailEntity> toEntityList(List<OptionDto.OptionDetailDto> dtoList, OptionEntity entity) {
        return dtoList.stream()
                .map(it -> toEntity(it, entity))
                .toList()
                ;
    }


    // 옵션 상세 Entity -> dto
    public OptionDto.OptionDetailDto toDto(OptionDetailEntity entity) {
        if (entity == null) {
            return null;
        }

        return OptionDto.OptionDetailDto.builder()
                .opDtId(entity.getOpDtId())
                .opDtName(entity.getOpDtName())
                .plusTime(entity.getOpDtPlusTime())
                .plusCost(entity.getOpDtPlusCost())
                .build()
                ;
    }

    public List<OptionDto.OptionDetailDto> toDtoList(List<OptionDetailEntity> entityList) {
        return entityList.stream()
                .map(this::toDto)
                .toList()
                ;
    }

}
