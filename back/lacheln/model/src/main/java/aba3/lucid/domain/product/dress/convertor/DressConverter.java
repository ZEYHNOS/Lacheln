package aba3.lucid.domain.product.dress.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.ifs.ConverterIfs;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.dto.DressResponse;
import aba3.lucid.domain.product.dress.entity.DressSizeEntity;
import aba3.lucid.domain.product.dto.option.OptionDto;
import aba3.lucid.domain.product.entity.HashtagEntity;
import aba3.lucid.domain.product.entity.OptionEntity;

import java.util.List;

@Converter
public class DressConverter implements ConverterIfs<DressEntity, DressRequest, DressResponse> {

    @Override
    public DressResponse toResponse(DressEntity entity) {
        if (entity == null) {
            return null;
        }

        return DressResponse.builder().build();
    }

    @Override
    public DressEntity toEntity(DressRequest req) {
        if (req == null) {
            return null;
        }

        DressEntity entity = DressEntity.builder()
                .pdName(req.getName())
                .pdRec(req.getRec())
                .pdPrice(req.getPrice())
                .pdStatus(req.getStatus())
                .pdTaskTime(req.getTaskTime())
                .pdDescription(req.getDescription())
                .dressColor(req.getColor())
                .dressInAvailable(req.getInAvailable())
                .dressOutAvailable(req.getOutAvailable())
                .build()
                ;


        entity.getHashtagList().addAll(toHashTagEntityList(req.getHashTagList(), entity));
        entity.getOpList().addAll(toOptionEntityList(req.getOptionList()));
        // TODO dressSizeList converter 만들기

        return entity;
    }

    // TODO 흠.. Converter 분리해야할려나
    public List<OptionEntity> toOptionEntityList(List<OptionDto> optionDtoList) {
        if (optionDtoList == null || optionDtoList.size() == 0) {
            return List.of();
        }

        return optionDtoList.stream()
                .map(it -> OptionEntity.builder()
                        .opName(it.getName())
//                        .opDtList(it.getOptionDtList())
                        .opStatus(it.getStatus())
                        .opEssential(it.getEssential())
                        .opOverlap(it.getOverlap())
                        .build())
                .toList();
    }

    public List<HashtagEntity> toHashTagEntityList(List<String> hashTagDtoList, DressEntity dress) {
        if (hashTagDtoList == null || hashTagDtoList.size() == 0) {
            return List.of();
        }

        return hashTagDtoList.stream()
                .map(tag -> HashtagEntity.builder()
                .tagName(tag)
                .product(dress)
                .build())
                .toList()
                ;
    }
}
