package aba3.lucid.domain.packages.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.description.AbstractDescriptionConverter;
import aba3.lucid.domain.packages.entity.PackageDescriptionEntity;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.product.dto.DescriptionRequest;

@Converter
public class PackageDescriptionConverter extends AbstractDescriptionConverter<PackageEntity, PackageDescriptionEntity> {
    @Override
    public PackageDescriptionEntity toDescriptionEntity(DescriptionRequest request, PackageEntity entity, int order) {
        return PackageDescriptionEntity.builder()
                .entity(entity)
                .type(request.getType())
                .value(request.getValue())
                .order(order)
                .build()
                ;
    }
}
