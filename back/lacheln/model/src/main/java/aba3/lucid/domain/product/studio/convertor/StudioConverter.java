package aba3.lucid.domain.product.studio.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.converter.HashtagConverter;
import aba3.lucid.domain.product.converter.OptionConverter;
import aba3.lucid.domain.product.converter.ProductConverter;
import aba3.lucid.domain.product.converter.ProductImageConverter;
import aba3.lucid.domain.product.studio.dto.StudioRequest;
import aba3.lucid.domain.product.studio.dto.StudioResponse;
import aba3.lucid.domain.product.studio.entity.StudioEntity;

@Converter
public class StudioConverter extends ProductConverter<StudioEntity, StudioRequest, StudioResponse> {

    public StudioConverter(OptionConverter optionConverter, HashtagConverter hashtagConverter, ProductImageConverter productImageConverter) {
        super(optionConverter, hashtagConverter, productImageConverter);
    }

    @Override
    protected StudioEntity createEntity(StudioRequest request, CompanyEntity company) {
        return StudioEntity.builder()
                .company(company)
                .pdName(request.getName())
                .pdRec(request.getRec())
                .pdPrice(request.getPrice())
                .pdStatus(request.getStatus())
                .pdTaskTime(request.getTaskTime())
                .pdDescription(request.getDescription())
                .stdBgOptions(request.getStdBgOptions())
                .stdInAvailable(request.getStdInAvailable())
                .stdMaxPeople(request.getStdMaxPeople())
                .stdOutAvailable(request.getStdOutAvailable())
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
                .description(entity.getPdDescription())
                .rec(entity.getPdRec())
                .taskTime(entity.getPdTaskTime())
                .stdOutAvailable(entity.getStdOutAvailable())
                .stdInAvailable(entity.getStdInAvailable())
                .stdBgOptions(entity.getStdBgOptions())
                .stdMaxPeople(entity.getStdMaxPeople())
                .hashTagList(hashtagConverter.toDtoList(entity.getHashtagList()))
                .optionList(optionConverter.toDtoList(entity.getOpList()))
                .productImageUrl(productImageConverter.toDtoList(entity.getImageList()))
                .build()
                ;
    }
}
