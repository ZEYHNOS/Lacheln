package aba3.lucid.domain.packages.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.dto.PackageRegisterRequest;
import aba3.lucid.domain.packages.dto.PackageResponse;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.product.enums.PackageStatus;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Converter
@RequiredArgsConstructor
public class PackageConverter {

    private final PackageToProductConverter packageToProductConverter;
    private final PackageDescriptionConverter packageDescriptionConverter;

    public List<PackageResponse> toResponseList(List<PackageEntity> entityList) {
        return entityList.stream()
                .map(this::toResponse)
                .toList()
                ;
    }

    public PackageEntity toEntity(PackageRegisterRequest request, CompanyEntity admin) {
        return PackageEntity.builder()
                .packName(request.getPackageName())
                .packageDescriptionEntityList(List.of())
                .packAdmin(admin)
                .packImageUrl("/default/images/package.png")
                .packDiscountrate(0)
                .packCreateDate(LocalDateTime.now())
                .packEndDate(LocalDateTime.of(2099, 12, 31, 0, 0, 0))
                .packStatus(PackageStatus.SETTING)
                .build()
                ;

    }


    public PackageResponse toResponse(PackageEntity entity) {
        return PackageResponse.builder()
                .packageId(entity.getPackId())
                .name(entity.getPackName())
                .discountrate(entity.getPackDiscountrate())
                .admin(packageToProductConverter.toResponse(entity.getPackId(), entity.getPackAdmin()))
                .cp1(packageToProductConverter.toResponse(entity.getPackId(), entity.getPackCompany1()))
                .cp2(packageToProductConverter.toResponse(entity.getPackId(), entity.getPackCompany2()))
                .createAt(entity.getPackCreateDate())
                .endDate(entity.getPackEndDate())
                .status(entity.getPackStatus())
                .imageUrl(entity.getPackImageUrl())
                .descriptionResponseList(packageDescriptionConverter.toDescriptionResponseList(entity.getPackageDescriptionEntityList()))
                .build()
                ;
    }

}
