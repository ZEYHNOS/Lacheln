package aba3.lucid.domain.product.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.description.AbstractDescriptionConverter;
import aba3.lucid.domain.product.dto.DescriptionRequest;
import aba3.lucid.domain.product.entity.ProductDescriptionEntity;
import aba3.lucid.domain.product.entity.ProductEntity;

@Converter
public class ProductDescriptionConverter extends AbstractDescriptionConverter<ProductEntity, ProductDescriptionEntity> {

    @Override
    public ProductDescriptionEntity toDescriptionEntity(DescriptionRequest request, ProductEntity entity, int order) {
        return ProductDescriptionEntity.builder()
                .product(entity)
                .type(request.getType())
                .value(request.getValue())
                .order(order)
                .build()
                ;
    }

}
