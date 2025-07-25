package aba3.lucid.domain.product.makeup.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.converter.*;
import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.domain.product.makeup.dto.MakeUpResponse;
import aba3.lucid.domain.product.makeup.dto.MakeupRequest;
import aba3.lucid.domain.product.makeup.entity.MakeupEntity;

@Converter
public class MakeUpConverter extends ProductAbstractConverter<MakeupEntity, MakeupRequest, MakeUpResponse> {

    public MakeUpConverter(OptionConverter optionConverter
            , HashtagConverter hashtagConverter
            , ProductImageConverter productImageConverter
            , ProductDescriptionConverter descriptionConverter) {
        super(optionConverter, hashtagConverter, productImageConverter, descriptionConverter);
    }

    @Override
    protected MakeupEntity createEntity(MakeupRequest request, CompanyEntity company) {
        return MakeupEntity.builder()
                .company(company)
                .pdName(request.getName().trim())
                .pdRec(request.getRec())
                .pdPrice(request.getPrice())
                .pdStatus(ProductStatus.INACTIVE)
                .pdTaskTime(request.getTaskTime())
                .makeupVisit(request.getVisit())
                .makeupManager(request.getManager())
                .makeupBt(request.getBusinessTrip())
                .build()
                ;
    }

    @Override
    protected MakeUpResponse createResponse(MakeupEntity entity) {
        return MakeUpResponse.builder()
                .id(entity.getPdId())
                .cpId(entity.getCompany().getCpId())
                .name(entity.getPdName())
                .price(entity.getPdPrice())
                .status(entity.getPdStatus())
                .rec(entity.getPdRec())
                .category(entity.getCompany().getCpCategory())
                .taskTime(entity.getPdTaskTime())
                .visit(entity.getMakeupVisit())
                .manager(entity.getMakeupManager())
                .businessTrip(entity.getMakeupBt())
                .descriptionList(descriptionConverter.toDescriptionResponseList(entity.getProductDescriptionEntityList()))
                .hashTagList(hashtagConverter.toDtoList(entity.getHashtagList()))
                .optionList(optionConverter.toDtoList(entity.getOpList()))
                .productImageUrl(productImageConverter.toDtoList(entity.getImageList()))
                .build()
                ;
    }
}
