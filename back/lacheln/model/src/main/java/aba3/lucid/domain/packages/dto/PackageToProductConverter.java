package aba3.lucid.domain.packages.dto;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.packages.entity.PackageToProductEntity;
import aba3.lucid.domain.product.entity.ProductEntity;

@Converter
public class PackageToProductConverter {

    public PackageToProductEntity toEntity(ProductEntity product, PackageEntity packageEntity) {
        return PackageToProductEntity.builder()
                .product(product)
                .packageEntity(packageEntity)
                .build()
                ;
    }

}
