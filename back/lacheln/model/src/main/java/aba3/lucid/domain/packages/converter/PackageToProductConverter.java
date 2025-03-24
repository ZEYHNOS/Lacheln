package aba3.lucid.domain.packages.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.packages.entity.PackageToProductEntity;
import aba3.lucid.domain.product.entity.ProductEntity;

@Converter
public class PackageToProductConverter {

    public PackageToProductEntity toEntity(PackageEntity packageEntity, ProductEntity productEntity) {
        return PackageToProductEntity.builder()
                .packageEntity(packageEntity)
                .product(productEntity)
                .build()
                ;
    }

}
