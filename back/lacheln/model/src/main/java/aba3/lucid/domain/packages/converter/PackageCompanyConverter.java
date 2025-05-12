package aba3.lucid.domain.packages.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.dto.PackageCompanyResponse;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.product.entity.ProductEntity;

@Converter
public class PackageCompanyConverter {

    public PackageCompanyResponse toResponse(ProductEntity product, CompanyEntity company) {
        return PackageCompanyResponse.builder()
                .id(company.getCpId())
                .email(company.getCpEmail())
                .name(company.getCpName())
                .address(company.getCpAddress())
                .category(company.getCpCategory())
                .profileImageUrl(company.getCpProfile())
                .productId(product.getPdId())
                .productName(product.getPdName())
                .productPrice(product.getPdPrice())
                .build()
                ;
    }

}
