package aba3.lucid.domain.packages.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.dto.PackageCompanyResponse;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.packages.entity.PackageToProductEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.repository.PackageToProductRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Converter
@RequiredArgsConstructor
public class PackageToProductConverter {

    private final PackageToProductRepository packageToProductRepository;

    public PackageToProductEntity toEntity(PackageEntity packageEntity, ProductEntity productEntity) {
        return PackageToProductEntity.builder()
                .packageEntity(packageEntity)
                .product(productEntity)
                .cpId(productEntity.getCompany().getCpId())
                .build()
                ;
    }

    public PackageCompanyResponse toResponse(Long packId, CompanyEntity company) {
        Optional<PackageToProductEntity> optionalPackageToProductEntity = packageToProductRepository.findByPackageEntity_PackIdAndCpId(packId, company.getCpId());
        if (optionalPackageToProductEntity.isPresent()) {
            PackageToProductEntity packageToProduct = optionalPackageToProductEntity.get();

            return PackageCompanyResponse.builder()
                    .id(company.getCpId())
                    .email(company.getCpEmail())
                    .address(company.getCpAddress())
                    .name(company.getCpName())
                    .category(company.getCpCategory())
                    .profileImageUrl(company.getCpProfile())
                    .productId(packageToProduct.getProduct().getPdId())
                    .productName(packageToProduct.getProduct().getPdName())
                    .productPrice(packageToProduct.getProduct().getPdPrice())
                    .build()
                    ;
        }

        return PackageCompanyResponse.builder()
                .id(company.getCpId())
                .email(company.getCpEmail())
                .address(company.getCpAddress())
                .name(company.getCpName())
                .category(company.getCpCategory())
                .profileImageUrl(company.getCpProfile())
                .build()
                ;
    }
}
