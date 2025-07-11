package aba3.lucid.domain.packages.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.image.RepresentativeImage;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.dto.PackageCompanyResponse;
import aba3.lucid.domain.packages.dto.PackageProductResponse;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.packages.entity.PackageToProductEntity;
import aba3.lucid.domain.product.converter.OptionConverter;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.domain.product.dto.option.OptionDto;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.makeup.entity.MakeupEntity;
import aba3.lucid.domain.product.repository.PackageToProductRepository;
import aba3.lucid.domain.product.studio.entity.StudioEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Converter
@RequiredArgsConstructor
public class PackageToProductConverter {

    private final PackageToProductRepository packageToProductRepository;
    private final OptionConverter optionConverter;

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

            PackageCompanyResponse response = PackageCompanyResponse.builder()
                    .id(company.getCpId())
                    .email(company.getCpEmail())
                    .address(company.getCpAddress())
                    .name(company.getCpName())
                    .category(company.getCpCategory())
                    .profileImageUrl(company.getCpProfile())
                    .productId(packageToProduct.getProduct().getPdId())
                    .productName(packageToProduct.getProduct().getPdName())
                    .productPrice(packageToProduct.getProduct().getPdPrice())
                    .optionDtoList(optionConverter.toDtoList(packageToProduct.getProduct().getOpList()))
                    .build()
                    ;

            updateDiffOptions(response.getOptionDtoList(), company, packageToProduct.getProduct());

            return response;
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

    public PackageProductResponse toPackageProductResponse(PackageToProductEntity packageToProduct) {
        PackageProductResponse response = PackageProductResponse.builder()
                .cpId(packageToProduct.getProduct().getCompany().getCpId())
                .pdId(packageToProduct.getProduct().getPdId())
                .companyName(packageToProduct.getProduct().getCompany().getCpName())
                .productName(packageToProduct.getProduct().getPdName())
                .category(packageToProduct.getProduct().getCompany().getCpCategory())
                .price(packageToProduct.getProduct().getPdPrice())
                .imageUrl(RepresentativeImage.getRepresentativeImage(packageToProduct.getProduct().getImageList()))
                .optionDtoList(optionConverter.toDtoList(packageToProduct.getProduct().getOpList()))
                .build()
                ;

        response.setOptionDtoList(updateDiffOptions(response.getOptionDtoList(), packageToProduct.getProduct().getCompany(), packageToProduct.getProduct()));

        return response;
    }

    public List<PackageProductResponse> toPackageProductResponseList(List<PackageToProductEntity> packageToProductEntityList) {
        if (packageToProductEntityList == null || packageToProductEntityList.isEmpty()) {
            return null;
        }

        return packageToProductEntityList.stream()
                .map(this::toPackageProductResponse)
                .toList()
                ;
    }

    private List<OptionDto> updateDiffOptions(List<OptionDto> optionDtoList, CompanyEntity company, ProductEntity product) {
        if (optionDtoList == null || optionDtoList.isEmpty()) {
            return null;
        }

        Object unproxied = Hibernate.unproxy(product);

        if (unproxied instanceof DressEntity dress) {
            optionDtoList = updateDressOption(optionDtoList, dress);
        }

        switch (company.getCpCategory()) {
            case S -> {
                if (product instanceof StudioEntity studio) {
                    updateStudioOption(optionDtoList, studio);
                }
            }
            case M -> {
                if (product instanceof MakeupEntity makeup) {
                    updateMakeupOption(optionDtoList, makeup);
                }
            }
            case D -> {
                if (product instanceof DressEntity dress) {
                    updateDressOption(optionDtoList, dress);
                }
            }
        }

        return optionDtoList;
    }

    private List<OptionDto> updateDressOption(List<OptionDto> optionDtoList, DressEntity dress) {
        if (optionDtoList == null || optionDtoList.isEmpty()) {
            return null;
        }

        List<OptionDto> result = new ArrayList<>(optionDtoList);
        result.add(
                OptionDto.builder()
                        .name("사이즈")
                        .essential(BinaryChoice.Y)
                        .overlap(BinaryChoice.Y)
                        .optionDtList(optionConverter.toDtoListByDress(dress))
                        .build()
        );

        return result;
    }

    private void updateMakeupOption(List<OptionDto> optionDtoList, MakeupEntity makeup) {

    }

    private void updateStudioOption(List<OptionDto> optionDtoList, StudioEntity studio) {

    }
}
