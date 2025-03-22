package aba3.lucid.domain.product.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.product.dto.option.ProductResponse;
import aba3.lucid.domain.product.entity.ProductEntity;

import java.util.List;

@Converter
public class ProductConverter {

    public ProductResponse toResponse(ProductEntity entity) {
        return ProductResponse.builder()
                .id(entity.getPdId())
                .companyName(entity.getCompany().getCpName())
                .productName(entity.getPdName())
                .price(entity.getPdPrice())
                .companyCategory(entity.getCompany().getCpCategory())
                .description(entity.getPdDescription())
                .build()
                ;
    }

    public List<ProductResponse> toResponseList(List<ProductEntity> productEntityList) {
        return productEntityList.stream()
                .map(this::toResponse)
                .toList()
                ;
    }
}
