package aba3.lucid.domain.product.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.entity.ProductImageEntity;

@Converter
public class ProductImageConverter {

    public ProductImageEntity toEntity(String imageUrl, ProductEntity product) {
        return ProductImageEntity.builder()
                .product(product)
                .pdImageUrl(imageUrl)
                .build()
                ;
    }


    public String toDto(ProductImageEntity entity) {
        return entity.getPdImageUrl();
    }

}
