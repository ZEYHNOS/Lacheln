package aba3.lucid.common.image;

import aba3.lucid.domain.product.entity.ProductImageEntity;

import java.util.List;

public class RepresentativeImage {

    public static String getRepresentativeImage(List<ProductImageEntity> imageUrlList) {
        if (imageUrlList == null || imageUrlList.isEmpty()) {
            return "/product/default.png";
        }

        return imageUrlList.get(0).getPdImageUrl();
    }

}
