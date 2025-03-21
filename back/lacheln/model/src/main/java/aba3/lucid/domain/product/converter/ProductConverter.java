package aba3.lucid.domain.product.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.ifs.ProductConverterIfs;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.dto.ProductRequest;
import aba3.lucid.domain.product.dto.ProductResponse;
import aba3.lucid.domain.product.entity.HashtagEntity;
import aba3.lucid.domain.product.entity.OptionEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.entity.ProductImageEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Converter
@RequiredArgsConstructor
public abstract class ProductConverter<ENTITY extends ProductEntity, REQ extends ProductRequest, RES extends ProductResponse>
        implements ProductConverterIfs<ENTITY, REQ, RES> {

    protected final OptionConverter optionConverter;
    protected final HashtagConverter hashtagConverter;
    protected final ProductImageConverter productImageConverter;


    public ENTITY toEntity(REQ request, CompanyEntity company) {
        if (company == null || request == null) {
            throw new ApiException(ErrorCode.NULL_POINT);
        }

        ENTITY entity = createEntity(request, company);

        List<OptionEntity> optionEntityList = optionConverter.toEntityList(request.getOptionList(), entity);
        List<HashtagEntity> hashtagEntityList = hashtagConverter.toEntityList(request.getHashTagList(), entity);
        List<ProductImageEntity> productImageEntityList = productImageConverter.toEntityList(request.getImageUrlList(), entity);

        entity.updateFormList(optionEntityList, hashtagEntityList, productImageEntityList);
        return entity;
    }

    public RES toResponse(ENTITY entity) {
        if (entity == null) {
            throw new ApiException(ErrorCode.NULL_POINT);
        }

        return createResponse(entity);
    }


    protected abstract ENTITY createEntity(REQ request, CompanyEntity company);
    protected abstract RES createResponse(ENTITY entity);


}
