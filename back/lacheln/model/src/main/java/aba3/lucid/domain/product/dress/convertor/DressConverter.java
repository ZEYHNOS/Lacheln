package aba3.lucid.domain.product.dress.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.ifs.ConverterIfs;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.converter.HashtagConverter;
import aba3.lucid.domain.product.converter.OptionConverter;
import aba3.lucid.domain.product.converter.ProductImageConverter;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.dto.DressResponse;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.domain.product.entity.HashtagEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Converter
@RequiredArgsConstructor
public class DressConverter implements ConverterIfs<DressEntity, DressRequest, DressResponse> {

    private final OptionConverter optionConverter;
    private final DressSizeConverter dressSizeConverter;
    private final ProductImageConverter productImageConverter;
    private final HashtagConverter hashtagConverter;

    @Override
    public DressResponse toResponse(DressEntity entity) {
        if (entity == null) {
            return null;
        }

        return DressResponse.builder()
                .name(entity.getPdName())
                .price(entity.getPdPrice())
                .color(entity.getDressColor())
                .status(entity.getPdStatus())
                .outAvailable(entity.getDressOutAvailable())
                .inAvailable(entity.getDressInAvailable())
                .description(entity.getPdDescription())
                .rec(entity.getPdRec())
                .taskTime(entity.getPdTaskTime())
                .hashTagList(hashtagConverter.toDtoList(entity.getHashtagList()))
                .optionList(optionConverter.toDtoList(entity.getOpList()))
                .dressSizeList(dressSizeConverter.toDtoList(entity.getDressSizeList()))
                .productImageUrl(productImageConverter.toDtoList(entity.getImageList()))
                .build()
                ;
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

        // 기존 해시태그 옵션, 드래스 사이즈, 이미지 리스트 초기화하고 저장
        entity.updateHashTag(hashtagConverter.toEntityList(req.getHashTagList(), entity));
        entity.updateOptionList(optionConverter.toEntityList(req.getOptionList(), entity));
        entity.updateDressSizeList(dressSizeConverter.toEntityList(req.getSizeList(), entity));
        entity.updateProductImage(productImageConverter.toEntityList(req.getImageUrlList(), entity));

        return entity;
    }

    // Company 정보 또한 저장해야 하기 때문에 오버로드
    public DressEntity toEntity(DressRequest req, CompanyEntity company) {
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

        // 기존 해시태그 옵션, 드래스 사이즈, 이미지 리스트 초기화하고 저장
        entity.updateHashTag(hashtagConverter.toEntityList(req.getHashTagList(), entity));
        entity.updateOptionList(optionConverter.toEntityList(req.getOptionList(), entity));
        entity.updateDressSizeList(dressSizeConverter.toEntityList(req.getSizeList(), entity));
        entity.updateProductImage(productImageConverter.toEntityList(req.getImageUrlList(), entity));

        return entity;
    }

    public List<HashtagEntity> toHashTagEntityList(List<String> hashTagDtoList, DressEntity dress) {
        if (hashTagDtoList.isEmpty()) {
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
