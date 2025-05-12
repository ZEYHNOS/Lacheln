package aba3.lucid.domain.product.studio.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.converter.*;
import aba3.lucid.domain.product.studio.dto.StudioRequest;
import aba3.lucid.domain.product.studio.dto.StudioResponse;
import aba3.lucid.domain.product.studio.entity.StudioEntity;

@Converter
public class StudioConverter extends ProductAbstractConverter<StudioEntity, StudioRequest, StudioResponse> {

    public StudioConverter(OptionConverter optionConverter
            , HashtagConverter hashtagConverter
            , ProductImageConverter productImageConverter
            , ProductDescriptionConverter descriptionConverter) {
        super(optionConverter, hashtagConverter, productImageConverter, descriptionConverter);
    }

    @Override
    protected StudioEntity createEntity(StudioRequest request, CompanyEntity company) {
        return StudioEntity.builder()
                .company(company)
                .pdName(request.getName().trim())
                .pdRec(request.getRec())
                .pdPrice(request.getPrice())
                .pdStatus(request.getStatus())
                .pdTaskTime(request.getTaskTime())
                .stdBgOptions(request.getBgOptions())
                .stdInAvailable(request.getInAvailable())
                .stdMaxPeople(request.getMaxPeople())
                .stdOutAvailable(request.getOutAvailable())
                .build()
                ;
    }

    @Override
    protected StudioResponse createResponse(StudioEntity entity) {
        return StudioResponse.builder()
                .id(entity.getPdId())
                .name(entity.getPdName())
                .price(entity.getPdPrice())
                .status(entity.getPdStatus())
                .rec(entity.getPdRec())
                .taskTime(entity.getPdTaskTime())
                .outAvailable(entity.getStdOutAvailable())
                .inAvailable(entity.getStdInAvailable())
                .bgOptions(entity.getStdBgOptions())
                .maxPeople(entity.getStdMaxPeople())
                .descriptionList(descriptionConverter.toDescriptionResponseList(entity.getProductDescriptionEntityList()))
                .hashTagList(hashtagConverter.toDtoList(entity.getHashtagList()))
                .optionList(optionConverter.toDtoList(entity.getOpList()))
                .productImageUrl(productImageConverter.toDtoList(entity.getImageList()))
                .build()
                ;
    }
}
