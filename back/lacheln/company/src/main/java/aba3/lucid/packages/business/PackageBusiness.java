package aba3.lucid.packages.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.converter.PackToCpConverter;
import aba3.lucid.domain.packages.converter.PackageConverter;
import aba3.lucid.domain.packages.dto.PackageProductRequest;
import aba3.lucid.domain.packages.dto.PackageRequest;
import aba3.lucid.domain.packages.dto.PackageResponse;
import aba3.lucid.domain.packages.dto.PackageToProductConverter;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.packages.entity.PackageToCompanyEntity;
import aba3.lucid.packages.service.PackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class PackageBusiness {

    private final PackageService packageService;
    private final CompanyService companyService;

    private final PackageConverter packageConverter;
    private final PackToCpConverter packToCpConverter;
    private final PackageToProductConverter packageToProductConverter;

    // 패키지 등록
    public PackageResponse packageRegister(PackageRequest packageRequest, long companyId) {
        log.info("PackageRequest : {}", packageRequest);
        Validator.throwIfNull(packageRequest);

        // TODO 누가 방장인지 정해야 할 듯
        CompanyEntity companyEntity = companyService.findByIdWithThrow(companyId);


        PackageEntity entity = packageConverter.toEntity(packageRequest);
        log.info("PackageRequest To PackageEntity : {}", entity);
        PackageEntity newEntity = packageService.packageRegister(entity);
        log.info("Saved PackageEntity : {}", entity);

        PackageResponse packageResponse = packageConverter.toResponse(newEntity);


        // 패키지 방에 저장하기
        PackageToCompanyEntity packageToCompany = packToCpConverter.toEntity(newEntity, companyEntity);
        packageService.invitationPackageGroup(packageToCompany);
        log.info("PackageEntity To PackageResponse : {}", packageResponse);

        return packageResponse;
    }


    // 패키지 그룹 초대하기(강제 초대)
    public void invitationPackageGroup(long packageId, long companyId) {
        // 방장 제외하고도 초대 가능하나?

        log.info("packId : {}", packageId);
        log.info("companyID : {}", companyId);
        Validator.throwIfInvalidId(packageId, companyId);

        long count = packageService.countByPackageInCompany(packageId);
        log.info("PackageInCompanyCount : {}", count);
        if (count >= 3) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "패키지 하나에 3개의 업체만 존재할 수 있습니다");
        }

        // TODO 이미 초대가 되어있는지 확인하기

        // 패키지, 업체 Entity 가지고 오기
        PackageEntity packageEntity = packageService.findPackageEntityById(packageId);
        CompanyEntity companyEntity = companyService.findByIdWithThrow(companyId);

        PackageToCompanyEntity entity = packToCpConverter.toEntity(packageEntity, companyEntity);

        // 저장하기
        PackageToCompanyEntity newEntity = packageService.invitationPackageGroup(entity);
        log.info("PackageToCompanyEntity : {}", newEntity);
    }

    // 패키지 상품 등록하기
    public void productRegister(PackageProductRequest request, long companyId) {
        // 유효성 검사
        Validator.throwIfNull(request);
        Validator.throwIfInvalidId(companyId);

        // 해당 업체의 상품인지 확인하기

        // 패키지에 소속된 업체인지 확인하기

        // 이미 다른 상품이 등록되었는지

        // toEntity

        // save

        // ProductStatus -> package

        // TODO return Response

    }

    // TODO Filter
    public List<PackageResponse> getPackageList() {
        return packageConverter.toResponseList(packageService.getPackageList());
    }


    // 패키지 상품 삭제



    // 패키지 승인 요청(PackageToCompany Status 변경?)



}
