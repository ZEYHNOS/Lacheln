package aba3.lucid.domain.product.dress.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.ifs.ConverterIfs;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.converter.OptionConverter;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.dto.DressResponse;
import aba3.lucid.domain.product.dress.entity.DressSizeEntity;
import aba3.lucid.domain.product.dto.option.OptionDto;
import aba3.lucid.domain.product.entity.HashtagEntity;
import aba3.lucid.domain.product.entity.OptionEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Converter
@RequiredArgsConstructor
public class DressConverter implements ConverterIfs<DressEntity, DressRequest, DressResponse> {

    private final OptionConverter optionConverter;
    private final DressSizeConverter dressSizeConverter;

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
                // 해시태그 entity -> dto
                .hashTagList(entity.getHashtagList().stream()
                        .map(HashtagEntity::getTagName)
                        .toList())
                .taskTime(entity.getPdTaskTime())
                // 옵션, 옵션 상세 entity -> dto
                .optionList(entity.getOpList().stream()
                        .map(optionConverter::toDto)
                        .toList())
                // 드레스 사이즈 entity -> Dto
                .dressSizeList(entity.getDressSizeList().stream()
                        .map(dressSizeConverter::toDto)
                        .toList())
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

        // 기존 해시태그 삭제 후 다시 저장
        entity.setHashtagList(toHashTagEntityList(req.getHashTagList(), entity));
        // 기존 옵션 삭제 후 저장
        entity.setOptionList(req.getOptionList().stream()
                .map(it -> optionConverter.toEntity(it, entity)).toList());
        // 기존 드레스 사이즈 리스트 삭제 후 저장
        entity.setDressSizeList(req.getSizeList().stream()
                .map(dressSizeConverter::toEntity)
                .toList());

        return entity;
    }

    // Company 정보 또한 저장해야 하기 때문에 오버로드
    public DressEntity toEntity(DressRequest req, CompanyEntity company) {
        if (req == null) {
            return null;
        }

        DressEntity entity = DressEntity.builder()
                .company(company)
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


        // 기존 해시태그 삭제 후 다시 저장
        entity.setHashtagList(toHashTagEntityList(req.getHashTagList(), entity));
        // 기존 옵션 삭제 후 저장
        entity.setOptionList(req.getOptionList().stream()
                .map(it -> optionConverter.toEntity(it, entity)).toList());
        // 기존 드레스 사이즈 리스트 삭제 후 저장
        entity.setDressSizeList(req.getSizeList().stream()
                .map(dressSizeConverter::toEntity)
                .toList());

        return entity;
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
