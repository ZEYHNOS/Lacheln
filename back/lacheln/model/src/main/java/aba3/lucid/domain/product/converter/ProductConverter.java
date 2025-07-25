package aba3.lucid.domain.product.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.image.RepresentativeImage;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.dto.ProductPackageInsertResponse;
import aba3.lucid.domain.packages.entity.PackageToProductEntity;
import aba3.lucid.domain.payment.dto.PopularDto;
import aba3.lucid.domain.product.dto.PopularResponse;
import aba3.lucid.domain.product.dto.ProductInfoResponse;
import aba3.lucid.domain.product.dto.option.ProductResponse;
import aba3.lucid.domain.product.entity.PopularEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import org.springframework.data.domain.Page;

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
        return ProductResponse.builder()
                .companyId(entity.getCompany().getCpId())
                .productId(entity.getPdId())
                .productName(entity.getPdName())
                .price(entity.getPdPrice())
                .imageUrl(RepresentativeImage.getRepresentativeImage(entity.getImageList()))
                .companyName(entity.getCompany().getCpName())
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

    public Page<ProductResponse> toResponsePage(Page<ProductEntity> productEntityPage) {
        return productEntityPage
                .map(this::toResponse)
                ;
    }

    public ProductResponse toResponse(ProductEntity product, CompanyEntity company) {
        return ProductResponse.builder()
                .productId(product.getPdId())
                .productName(product.getPdName())
                .price(product.getPdPrice())
                .status(product.getPdStatus())
                .companyName(company.getCpName())
                .imageUrl(RepresentativeImage.getRepresentativeImage(product.getImageList()))
                .build();
    }

    public List<PopularEntity> toEntityList(List<PopularDto> list) {
        return list.stream()
                .map(this::toEntity)
                .toList()
                ;
    }

    public PopularEntity toEntity(PopularDto dto) {
        return PopularEntity.builder()
                .companyId(dto.getCompanyId())
                .productId(dto.getProductId())
                .popularRank(dto.getRank())
                .build()
                ;
    }

    public ProductInfoResponse toInfoResponse(ProductEntity product, CompanyEntity company) {
        return ProductInfoResponse.builder()
                .productId(product.getPdId())
                .price(product.getPdPrice())
                .productName(product.getPdName())
                .status(product.getPdStatus())
                .imageUrl(RepresentativeImage.getRepresentativeImage(product.getImageList()))
                .companyId(company.getCpId())
                .companyName(company.getCpName())
                .category(company.getCpCategory())
                .build();
    }
}
