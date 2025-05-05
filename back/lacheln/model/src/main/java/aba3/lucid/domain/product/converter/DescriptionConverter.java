package aba3.lucid.domain.product.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.product.dto.DescriptionRequest;
import aba3.lucid.domain.product.dto.ProductDescriptionResponse;
import aba3.lucid.domain.product.entity.ProductDescriptionEntity;
import aba3.lucid.domain.product.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;

@Converter
public class DescriptionConverter {

    public ProductDescriptionEntity toEntity(ProductEntity product, DescriptionRequest request, int order) {
        return ProductDescriptionEntity.builder()
                .product(product)
                .type(request.getType())
                .value(request.getValue())
                .order(order)
                .build()
                ;
    }

    public List<ProductDescriptionEntity> toEntityList(ProductEntity product, List<DescriptionRequest> valueList) {
        List<ProductDescriptionEntity> result = new ArrayList<>();
        for (int i = 0; i < valueList.size(); i++) {
            result.add(toEntity(product, valueList.get(i), i));
        }

        return result;
    }

    public ProductDescriptionResponse toResponse(ProductDescriptionEntity entity) {
        return ProductDescriptionResponse.builder()
                .value(entity.getValue())
                .order(entity.getOrder())
                .type(entity.getType())
                .build()
                ;
    }

    public List<ProductDescriptionResponse> toResponseList(List<ProductDescriptionEntity> entityList) {
        return entityList.stream()
                .map(this::toResponse)
                .toList()
                ;
    }

}
