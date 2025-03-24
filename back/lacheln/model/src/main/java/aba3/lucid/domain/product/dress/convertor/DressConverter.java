package aba3.lucid.domain.product.dress.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.converter.HashtagConverter;
import aba3.lucid.domain.product.converter.OptionConverter;
import aba3.lucid.domain.product.converter.ProductAbstractConverter;
import aba3.lucid.domain.product.converter.ProductImageConverter;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.dto.DressResponse;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class DressConverter extends ProductAbstractConverter<DressEntity, DressRequest, DressResponse> {

    private final DressSizeConverter dressSizeConverter;

    public DressConverter(OptionConverter optionConverter
            , HashtagConverter hashtagConverter
            , ProductImageConverter productImageConverter
            , DressSizeConverter dressSizeConverter) {

        super(optionConverter, hashtagConverter, productImageConverter);
        this.dressSizeConverter = dressSizeConverter;
    }

    @Override
    protected DressEntity createEntity(DressRequest request, CompanyEntity company) {
        DressEntity entity = DressEntity.builder()
                .company(company)
                .pdName(request.getName().trim())
                .pdRec(request.getRec())
                .pdPrice(request.getPrice())
                .pdStatus(request.getStatus())
                .pdTaskTime(request.getTaskTime())
                .pdDescription(request.getDescription())
                .dressColor(request.getColor())
                .dressInAvailable(request.getInAvailable())
                .dressOutAvailable(request.getOutAvailable())
                .build();

        entity.updateDressSizeList(dressSizeConverter.toEntityList(request.getSizeList(), entity));
        return entity;
    }

    @Override
    protected DressResponse createResponse(DressEntity entity) {
        return DressResponse.builder()
                .id(entity.getPdId())
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
}
