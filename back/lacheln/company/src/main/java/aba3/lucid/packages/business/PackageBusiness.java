package aba3.lucid.packages.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.converter.PackToCpConverter;
import aba3.lucid.domain.packages.converter.PackageConverter;
import aba3.lucid.domain.packages.dto.PackageGroupCreateRequest;
import aba3.lucid.domain.packages.dto.PackageRequest;
import aba3.lucid.domain.packages.dto.PackageResponse;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.packages.entity.PackageToCompanyEntity;
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
    private final PackToCpConverter packToCpConverter;

    // 패키지 그룹 만들기
    public void createPackageGroup(PackageGroupCreateRequest request, long requestedCpId) {
        // 유효성 검사
        validatePackageGroupRequest(request, requestedCpId);


        PackageToCompanyEntity entity = packToCpConverter.toEntity(request);
        PackageToCompanyEntity newEntity = packageService.createPackageGroup(entity);
    }



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
        log.info("PackageEntity To PackageResponse : {}", packageResponse);

        return packageResponse;
    }


    // 패키지 그룹 생성 유효성 검사
    private void validatePackageGroupRequest(PackageGroupCreateRequest request, long requestedCpId) {
        Validator.throwIfNull(request);

        // 중복 ID 검사
        validateIdsAreUnique(request);

        // 카테고리 검사 및 요청한 업체가 포함되어 있는지
        validateCategoriesAreUnique(request, requestedCpId);
    }

    // 중복된 ID가 있는지 검사
    private void validateIdsAreUnique(PackageGroupCreateRequest request) {
        long id1 = request.getCompanyId1();
        long id2 = request.getCompanyId2();
        long id3 = request.getCompanyId3();

        Validator.throwIfInvalidId(id1, id2, id3);

        if (id1 == id2 || id2 == id3 || id1 == id3) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER, "중복된 아이디 값이 존재합니다.");
        }
    }

    // 중복된 카테고리가 있는지 검사 및 요청을 보낸 업체가 요청 데이터에 존재하는지
    private void validateCategoriesAreUnique(PackageGroupCreateRequest request, long requestedCpId) {
        long id1 = request.getCompanyId1();
        long id2 = request.getCompanyId2();
        long id3 = request.getCompanyId3();

        CompanyEntity cp1 = companyService.findByIdWithThrow(id1);
        CompanyEntity cp2 = companyService.findByIdWithThrow(id2);
        CompanyEntity cp3 = companyService.findByIdWithThrow(id3);

        if (cp1.getCpCategory() == cp2.getCpCategory() || cp2.getCpCategory() == cp3.getCpCategory() || cp1.getCpCategory() == cp3.getCpCategory()) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "중복된 카테고리의 업체가 둘 이상 존재합니다.");
        }

        if (!(requestedCpId == id1 || requestedCpId == id2 || requestedCpId == id3)) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER);
        }
    }
}
