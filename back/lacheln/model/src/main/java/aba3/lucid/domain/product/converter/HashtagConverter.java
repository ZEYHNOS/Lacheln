package aba3.lucid.domain.product.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.product.entity.HashtagEntity;
import aba3.lucid.domain.product.entity.ProductEntity;

import java.util.List;

@Converter
public class HashtagConverter {

    public List<HashtagEntity> toEntityList(List<String> hashtagList, ProductEntity product) {
        if (hashtagList == null || hashtagList.isEmpty()) {
            return null;
        }

        return hashtagList.stream()
                .map(it -> toEntity(it.trim(), product))
                .toList()
                ;
    }


    public List<String> toDtoList(List<HashtagEntity> hashtagEntityList) {
        if (hashtagEntityList == null || hashtagEntityList.isEmpty()) {
            return null;
        }

        return hashtagEntityList.stream()
                .map(HashtagEntity::getTagName)
                .toList()
                ;
    }


    public HashtagEntity toEntity(String hashtag, ProductEntity product) {
        return HashtagEntity.builder()
                .product(product)
                .tagName(hashtag)
                .build()
                ;
    }

    public String toDto(HashtagEntity entity) {
        return entity.getTagName();
    }

}
