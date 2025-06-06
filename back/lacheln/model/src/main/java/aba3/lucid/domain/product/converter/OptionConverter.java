package aba3.lucid.domain.product.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.domain.product.dress.entity.DressSizeEntity;
import aba3.lucid.domain.product.dto.option.OptionDto;
import aba3.lucid.domain.product.entity.OptionDetailEntity;
import aba3.lucid.domain.product.entity.OptionEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.enums.DressSize;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Converter
@RequiredArgsConstructor
public class OptionConverter {

    private final OptionDetailConverter optionDetailConverter;

    public List<OptionEntity> toEntityList(List<OptionDto> dtoList, ProductEntity entity) {
        return dtoList.stream()
                .map(it -> toEntity(it, entity))
                .toList()
                ;
    }

    public List<OptionDto> toDtoList(List<OptionEntity> entitieList) {
        return entitieList.stream()
                .map(this::toDto)
                .toList()
                ;
    }


    // 옵션 dto -> entity
    public OptionEntity toEntity(OptionDto dto, ProductEntity entity) {
        if (dto == null || entity == null) {
            return null;
        }

        if (dto.getName().equals("사이즈") && entity.getCompany().getCpCategory().equals(CompanyCategory.D)) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "사이즈라는 예약어 사용");
        }

        OptionEntity optionEntity = OptionEntity.builder()
                .product(entity)
                .opName(dto.getName().trim())
                .opOverlap(dto.getOverlap())
                .opEssential(dto.getEssential())
                .opStatus(dto.getStatus())
                .build()
                ;

        // 옵션 상세 dto -> entity
        optionEntity.setOptionDetailList(optionDetailConverter.toEntityList(dto.getOptionDtList(), optionEntity));

        return optionEntity;
    }

    // 옵션 entity -> dto
    public OptionDto toDto(OptionEntity entity) {
        if (entity == null) {
            return null;
        }

        return OptionDto.builder()
                .opId(entity.getOpId())
                .name(entity.getOpName())
                .essential(entity.getOpEssential())
                .overlap(entity.getOpOverlap())
                .status(entity.getOpStatus())
                .optionDtList(optionDetailConverter.toDtoList(entity.getOpDtList()))
                .build()
                ;
    }


    public List<OptionDto.OptionDetailDto> toDtoListByDress(DressEntity entity) {
        List<OptionDto.OptionDetailDto> result = new ArrayList<>();

        for (DressSizeEntity dressSize : entity.getDressSizeList()) {
            result.add(
                    OptionDto.OptionDetailDto.builder()
                            .opDtId(dressSize.getDressSizeId())
                            .opDtName(dressSize.getSize().toString())
                            .plusCost(dressSize.getPlusCost())
                            .plusTime(LocalTime.of(0, 0))
                            .build()
            );
        }

        return result;
    }

}
