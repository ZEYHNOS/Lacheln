package aba3.lucid.domain.product.dress.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.product.dress.dto.DressSizeDto;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.domain.product.dress.entity.DressSizeEntity;

import java.util.List;

@Converter
public class DressSizeConverter {

    public List<DressSizeEntity> toEntityList(List<DressSizeDto> dtoList, DressEntity entity) {
        return dtoList.stream()
                .map(it -> toEntity(it, entity))
                .toList()
        ;
    }

    public List<DressSizeDto> toDtoList(List<DressSizeEntity> entitieList) {
        return entitieList.stream()
                .map(this::toDto)
                .toList()
                ;
    }



    public DressSizeEntity toEntity(DressSizeDto dto, DressEntity dress) {
        return DressSizeEntity.builder()
                .size(dto.getSize())
                .dress(dress)
                .dressSizeStock(dto.getStock())
                .build()
                ;
    }


    public DressSizeDto toDto(DressSizeEntity entity) {
        return DressSizeDto.builder()
                .size(entity.getSize())
                .stock(entity.getDressSizeStock())
                .build()
                ;
    }

}
