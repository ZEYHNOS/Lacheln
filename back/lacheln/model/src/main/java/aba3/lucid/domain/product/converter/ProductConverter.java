package aba3.lucid.domain.product.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.packages.dto.ProductPackageInsertResponse;
import aba3.lucid.domain.packages.entity.PackageToProductEntity;
import aba3.lucid.domain.product.dto.option.ProductResponse;
import aba3.lucid.domain.product.entity.ProductEntity;

import java.util.List;

@Converter
public class ProductConverter {

    public ProductPackageInsertResponse toResponse(PackageToProductEntity entity) {
        return ProductPackageInsertResponse.builder()
                .packageName(entity.getPackageEntity().getPackName())
                .companyName(entity.getProduct().getCompany().getCpName())
                .productName(entity.getProduct().getPdName())
                .build()
                ;
    }

    public ProductResponse toResponse(ProductEntity entity) {
//        if (entity.getImageList().isEmpty()) {
//            throw new ApiException(ErrorCode.NULL_POINT);
//        }

        return ProductResponse.builder()
                .id(entity.getPdId())
                .name(entity.getPdName())
                .price(entity.getPdPrice())
                .imageUrl(entity.getImageList().get(0).getPdImageUrl())
                .status(entity.getPdStatus())
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
