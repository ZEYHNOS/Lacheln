package aba3.lucid.domain.product.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.product.dto.ProductImageResponse;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.entity.ProductImageEntity;

import java.util.List;

@Converter
public class ProductImageConverter {

    public List<ProductImageEntity> toEntityList(List<String> urlList, ProductEntity entity) {
        return urlList.stream()
                .map(it -> toEntity(it, entity))
                .toList()
                ;
    }

    public List<ProductImageResponse> toDtoList(List<ProductImageEntity> entityList) {
        return entityList.stream()
                .map(this::toDto)
                .toList()
                ;
    }

    public ProductImageResponse toDto(ProductImageEntity entity) {
        return ProductImageResponse.builder()
                .id(entity.getPdImageId())
                .url(entity.getPdImageUrl())
                .build()
                ;
    }

    public ProductImageEntity toEntity(String imageUrl, ProductEntity product) {
        return ProductImageEntity.builder()
                .product(product)
                .pdImageUrl(imageUrl)
                .build()
                ;
    }

}
