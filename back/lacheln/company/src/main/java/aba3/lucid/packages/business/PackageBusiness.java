package aba3.lucid.packages.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.converter.PackageConverter;
import aba3.lucid.domain.packages.dto.PackageRegisterRequest;
import aba3.lucid.domain.packages.dto.PackageResponse;
import aba3.lucid.domain.packages.dto.PackageUpdateRequest;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.packages.service.PackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Business
@RequiredArgsConstructor
public class PackageBusiness {

    private final PackageService packageService;
    private final CompanyService companyService;

    private final PackageConverter packageConverter;

    // 패키지 등록
    public PackageResponse packageRegister(PackageRegisterRequest request, Long adminId) {
        Validator.throwIfNull(request);
        Validator.throwIfInvalidId(adminId);

        CompanyEntity admin = companyService.findByIdWithThrow(adminId);
        PackageEntity packageEntity = packageConverter.toEntity(request, admin);

        // 패키지에 업체 정보 넣기
        CompanyEntity company1 = companyService.findByCpEmailWithThrow(request.getCpEmail1());
        CompanyEntity company2 = companyService.findByCpEmailWithThrow(request.getCpEmail2());
        packageEntity.setPackageInvitationCompanies(company1, company2);

        PackageEntity newEntity = packageService.packageRegister(packageEntity);

        return packageConverter.toResponse(newEntity);
    }

    // 패키지 정보 수정하기
    public PackageResponse packageUpdate(PackageUpdateRequest request, Long adminId, Long packageId) {
        Validator.throwIfNull(request);
        Validator.throwIfInvalidId(adminId, packageId);


        PackageEntity packageEntity = packageService.findByIdWithThrow(packageId);

        PackageEntity updateEntity = packageService.packageUpdate(packageEntity, request, adminId);

        return packageConverter.toResponse(updateEntity);
    }

    // 패키지 업로드
    public PackageResponse packageUpload(Long packageId, Long adminId) {
        Validator.throwIfInvalidId(packageId, adminId);

        PackageEntity packageEntity = packageService.findByIdWithThrow(packageId);

        PackageEntity uploadEntity = packageService.packageUpload(packageEntity, adminId);

        return packageConverter.toResponse(uploadEntity);
    }
}
