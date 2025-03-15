package aba3.lucid.domain.product.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.product.dto.option.OptionDto;
import aba3.lucid.domain.product.entity.OptionDetailEntity;
import aba3.lucid.domain.product.entity.OptionEntity;
import aba3.lucid.domain.product.entity.ProductEntity;

@Converter
public class OptionConverter {

    // 옵션 dto -> entity
    public OptionEntity toEntity(OptionDto dto, ProductEntity entity) {
        if (dto == null || entity == null) {
            return null;
        }

        OptionEntity optionEntity = OptionEntity.builder()
                .product(entity)
                .opName(dto.getName())
                .opOverlap(dto.getOverlap())
                .opEssential(dto.getEssential())
                .opStatus(dto.getStatus())
                .build()
                ;

        // 옵션 상세 dto -> entity
        optionEntity.setOptionDetailList(dto.getOptionDtList().stream()
                .map(it -> toEntity(it, optionEntity))
                .toList())
                ;

        return optionEntity;
    }

    // 옵션 entity -> dto
    public OptionDto toDto(OptionEntity entity) {
        if (entity == null) {
            return null;
        }

        return OptionDto.builder()
                .name(entity.getOpName())
                .essential(entity.getOpEssential())
                .overlap(entity.getOpOverlap())
                .status(entity.getOpStatus())
                .optionDtList(entity.getOpDtList().stream() // 옵션 상세 entity -> dto
                        .map(this::toDto)
                        .toList())
                .build()
                ;
    }


    // 옵션 상세 dto -> entity
    public OptionDetailEntity toEntity(OptionDto.OptionDetailDto dto, OptionEntity entity) {
        if (dto == null) {
            return null;
        }

        return OptionDetailEntity.builder()
                .option(entity)
                .opDtName(dto.getOpDtName())
                .opDtPlusCost(dto.getPlusCost())
                .opDtPlusTime(dto.getPlusTime())
                .build()
                ;
    }


    // 옵션 상세 Entity -> dto
    public OptionDto.OptionDetailDto toDto(OptionDetailEntity entity) {
        if (entity == null) {
            return null;
        }

        return OptionDto.OptionDetailDto.builder()
                .opDtName(entity.getOpDtName())
                .plusTime(entity.getOpDtPlusTime())
                .plusCost(entity.getOpDtPlusCost())
                .build()
                ;
    }

}
