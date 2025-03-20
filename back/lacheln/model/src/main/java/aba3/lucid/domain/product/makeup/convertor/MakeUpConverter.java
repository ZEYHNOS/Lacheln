package aba3.lucid.domain.product.makeup.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.converter.HashtagConverter;
import aba3.lucid.domain.product.converter.OptionConverter;
import aba3.lucid.domain.product.converter.ProductConverter;
import aba3.lucid.domain.product.converter.ProductImageConverter;
import aba3.lucid.domain.product.makeup.dto.MakeUpResponse;
import aba3.lucid.domain.product.makeup.dto.MakeupRequest;
import aba3.lucid.domain.product.makeup.entity.MakeupEntity;

@Converter
public class MakeUpConverter extends ProductConverter<MakeupEntity, MakeupRequest, MakeUpResponse> {

    public MakeUpConverter(OptionConverter optionConverter
            , HashtagConverter hashtagConverter
            , ProductImageConverter productImageConverter) {
        super(optionConverter, hashtagConverter, productImageConverter);
    }

    @Override
    protected MakeupEntity createEntity(MakeupRequest request, CompanyEntity company) {
        return MakeupEntity.builder()
                .company(company)
                .pdName(request.getName())
                .pdRec(request.getRec())
                .pdPrice(request.getPrice())
                .pdStatus(request.getStatus())
                .pdTaskTime(request.getTaskTime())
                .pdDescription(request.getDescription())
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
                .name(entity.getPdName())
                .price(entity.getPdPrice())
                .status(entity.getPdStatus())
                .description(entity.getPdDescription())
                .rec(entity.getPdRec())
                .taskTime(entity.getPdTaskTime())
                .visit(entity.getMakeupVisit())
                .manager(entity.getMakeupManager())
                .businessTrip(entity.getMakeupBt())
                .hashTagList(hashtagConverter.toDtoList(entity.getHashtagList()))
                .optionList(optionConverter.toDtoList(entity.getOpList()))
                .productImageUrl(productImageConverter.toDtoList(entity.getImageList()))
                .build()
                ;
    }
}
