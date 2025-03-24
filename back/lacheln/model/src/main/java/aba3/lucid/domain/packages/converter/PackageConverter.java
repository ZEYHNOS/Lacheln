package aba3.lucid.domain.packages.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.company.convertor.CompanyConvertor;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.dto.PackageRegisterRequest;
import aba3.lucid.domain.packages.dto.PackageResponse;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.product.enums.PackageStatus;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Converter
@RequiredArgsConstructor
public class PackageConverter {

    private final CompanyConvertor companyConvertor;

    public PackageEntity toEntity(PackageRegisterRequest request, CompanyEntity admin) {
        return PackageEntity.builder()
                .packName(request.getPackageName())
                .packComment(request.getPackageComment())
                .packAdmin(admin)
                .packImageUrl("static/default/images/package")
                .packDiscountrate(0)
                .packCreateDate(LocalDateTime.now())
                .packEndDate(LocalDateTime.of(2099, 12, 31, 0, 0, 0))
                .packStatus(PackageStatus.PRIVATE)
                .build()
                ;
    }


    public PackageResponse toResponse(PackageEntity entity) {
        return PackageResponse.builder()
                .id(entity.getPackId())
                .name(entity.getPackName())
                .comment(entity.getPackComment())
                .admin(companyConvertor.toResponse(entity.getPackAdmin()))
                .cp1(companyConvertor.toResponse(entity.getPackCompany1()))
                .cp2(companyConvertor.toResponse(entity.getPackCompany2()))
                .discountrate(entity.getPackDiscountrate())
                .createAt(entity.getPackCreateDate())
                .endDate(entity.getPackEndDate())
                .status(entity.getPackStatus())
                .build()
                ;
    }

}
