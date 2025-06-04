package aba3.lucid.packages.service;

import aba3.lucid.alert.service.CompanyAlertService;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.status_code.PackageErrorCode;
import aba3.lucid.common.status_code.ProductErrorCode;
import aba3.lucid.domain.description.AbstractDescriptionConverter;
import aba3.lucid.domain.packages.dto.PackageUpdateRequest;
import aba3.lucid.domain.packages.entity.PackageDescriptionEntity;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.packages.entity.PackageToProductEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.enums.PackageStatus;
import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.domain.product.repository.PackageRepository;
import aba3.lucid.domain.product.repository.PackageToProductRepository;
import aba3.lucid.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PackageService {

    private final ProductService productService;
    private final CompanyAlertService companyAlertService;

    private final PackageRepository packageRepository;
    private final PackageToProductRepository packageToProductRepository;
    private final AbstractDescriptionConverter<PackageEntity, PackageDescriptionEntity> descriptionConverter;

    // 패키지 등록
    @Transactional
    public PackageEntity packageRegister(PackageEntity packageEntity) {
        PackageEntity savedEntity = packageRepository.save(packageEntity);

        // 알림 보내기
        companyAlertService.sendAlertCompany(savedEntity);

        return savedEntity;
    }

    // 패키지에 상품 등록하기
    @Transactional
    public PackageToProductEntity productPackageInsert(PackageToProductEntity packToPdEntity
            , Long companyId
            , PackageEntity packageEntity
            , ProductEntity productEntity) {
        // 이미 해당 업체가 상품을 등록했는지
        if (existsByPackageEntityAndProduct(packageEntity, productEntity)) {
            throw new ApiException(PackageErrorCode.PRODUCT_ALREADY_REGISTERED);
        }

        // 등록하려는 상품이 해당 업체의 것인지
        if (productEntity.getCompany().getCpId() != companyId) {
            throw new ApiException(ProductErrorCode.NO_PRODUCT_OWNERSHIP);
        }

        // 상태가 비공개인지
        if (productEntity.getPdStatus() != ProductStatus.INACTIVE) {
            throw new ApiException(ProductErrorCode.PRODUCT_NOT_PRIVATE);
        }


        PackageToProductEntity newEntity = packageToProductRepository.save(packToPdEntity);

        // 상품 상태 패키지로 변경
        productService.updateStatusToPackage(newEntity.getProduct());

        return newEntity;
    }


    // 상품 등록 가능 여부 확인
    protected void throwIfImpossibleToPublicChange(PackageEntity entity, Long companyId) {
        // 방장의 요청이 아닐 때
        throwIfNotAdminRequest(entity, companyId);
        // 패키지 상태가 비공개가 아닐 때
        throwIfNotEqualsStatus(entity, PackageStatus.INACTIVE, "상품이 삭제되거나 이미 등록되어있습니다.");
        // 3가지 업체가 초대가 되었는지
        throwIfNotInvitedAllRequiredCompanies(entity);

        // 모든 상품이 등록되었는지
        if (countDistinctProductsByPackage(entity.getPackId()) != 3) {
            throw new ApiException(PackageErrorCode.INVALID_PACKAGE_REGISTRATION, "모든 상품이 등록되지 않았습니다.");
        }

        // 패키지 설명이 존재하는 지
        if (entity.getPackageDescriptionEntityList().isEmpty()) {
            throw new ApiException(PackageErrorCode.INVALID_PACKAGE_REGISTRATION, "패키지 설명이 존재하지 않습니다.");
        }

        // 패키지 대표 이미지가 있는지
        if (entity.getPackImageUrl() == null || entity.getPackImageUrl().isBlank()) {
            throw new ApiException(PackageErrorCode.INVALID_PACKAGE_REGISTRATION, "패키지의 대표 이미지가 없습니다.");
        }

        // 패키지 종료일이 등록되지 않았거나 오늘보다 하루 이상이지 않을 때
        LocalDateTime minimumDateTime = LocalDate.now().plusDays(1).atStartOfDay();
        if (entity.getPackEndDate().isBefore(minimumDateTime)) {
            throw new ApiException(PackageErrorCode.INVALID_PACKAGE_END_DATE, "패키지 종료 일자가 조건에 맞지 않습니다.");
        }
    }

    // 패키지 정보 수정
    @Transactional
    public PackageEntity packageUpdate(PackageEntity packageEntity, PackageUpdateRequest request, Long adminId) {
        // 정보 변경(상태는 변경 X)
        packageEntity.updateAdditionalField(request);
        packageEntity.updateDescription(descriptionConverter.toDescriptionEntityList(packageEntity, request.getDescriptionRequestList()));
        List<PackageToProductEntity> packageToProductList = packageToProductRepository.findAllByPackageEntity(packageEntity);

        // 모든 패키지 상품 작업 시간 통일
        updatePackageProductTaskTime(packageToProductList, request.getTaskTime());

        return packageRepository.save(packageEntity);
    }

    public void throwIfNotEqualsStatus(PackageEntity packageEntity, PackageStatus status) {
        if (packageEntity.getPackStatus().equals(status)) {
            return;
        }

        throw new ApiException(ErrorCode.BAD_REQUEST, "현재 패키지 상태에서 할 수 없습니다");
    }

    public void throwIfNotEqualsStatus(PackageEntity packageEntity, PackageStatus status, String description) {
        if (packageEntity.getPackStatus().equals(status)) {
            return;
        }

        throw new ApiException(ErrorCode.BAD_REQUEST, description);
    }

    // 패키지에 해당 회사가 존재하는지
    public PackageEntity findByPackIdAndCompanyIdWithThrow(Long packId, Long companyId) {
        return packageRepository.findByPackageIdAndCompanyId(packId, companyId)
                .orElseThrow(() -> new ApiException(PackageErrorCode.UNAUTHORIZED_PACKAGE_ACCESS));
    }

    // 패키지가 존재하는지
    public PackageEntity findByIdWithThrow(Long packageId) {
        return packageRepository.findById(packageId)
                .orElseThrow(() -> new ApiException(PackageErrorCode.PACKAGE_NOT_FOUND));
    }


    // 패키지에 상품을 등록했는지
    public boolean existsByPackageEntityAndProduct(PackageEntity packageEntity, ProductEntity productEntity) {
        return packageToProductRepository.existsByPackageEntity_packIdAndCpId(packageEntity.getPackId(), productEntity.getCompany().getCpId());
    }

    // 3개의 업체가 모두 초대가 되었는지
    private void throwIfNotInvitedAllRequiredCompanies(PackageEntity entity) {
        if (entity.getPackAdmin() == null || entity.getPackCompany1() == null || entity.getPackCompany2() == null) {
            throw new ApiException(PackageErrorCode.INVALID_PACKAGE_REGISTRATION, "모든 업체가 초대가 되어있지 않습니다.");
        }
    }

    // 현재 패키지에 등록된 상품 갯수
    private long countDistinctProductsByPackage(Long packageId) {
        return packageToProductRepository.countDistinctProductsByPackage(packageId);
    }


    // 패키지 업로드
    public PackageEntity packageUpload(PackageEntity packageEntity, Long adminId) {
        // 유효성 검사
        throwIfImpossibleToPublicChange(packageEntity, adminId);

        // 상태 변경 후 저장
        packageEntity.updatePackageStatus(PackageStatus.ACTIVE);
        return packageRepository.save(packageEntity);
    }

    // 방장의 요청이 아닐 때 에러 발생
    public void throwIfNotAdminRequest(PackageEntity packageEntity, Long adminId) {
        if (packageEntity.getPackAdmin().getCpId() != adminId) {
            throw new ApiException(PackageErrorCode.UNAUTHORIZED_PACKAGE_ACCESS);
        }
    }

    // 업체가 속한 패키지 리스트 출력
    public List<PackageEntity> getPackageList(Long companyId) {
        return packageRepository.findByCompanyIdInAnyRole(companyId);
    }

    public BigInteger getTotalPrice(Long packageId) {
        PackageEntity packageEntity = findByIdWithThrow(packageId);
        List<PackageToProductEntity> packageToProductEntityList = packageToProductRepository.findAllByPackageEntity(packageEntity);

        BigInteger result = BigInteger.ZERO;
        for (PackageToProductEntity entity : packageToProductEntityList) {
            result = result.add(entity.getProduct().getPdPrice());
        }

        return result;
    }

    // 패키지에서 상품 삭제하기
    @Transactional
    public void deletePackageProduct(Long packageId, Long companyId, Long productId) {
        // 해당 패키지의 해당 업체가 존재하면 Entity 가지고 오기
        PackageEntity packageEntity = findByPackIdAndCompanyIdWithThrow(packageId, companyId);

        ProductEntity product = productService.findByIdWithThrow(productId);

        // 해당 삼품의 소유가 아니라면 에러 (방장은 삭제할 수 있을려나?)
        productService.throwIfNotOwnProduct(product, companyId);

        // 이미 패키지가 PUBLIC 이면 BLOCK
        if (packageEntity.getPackStatus().equals(PackageStatus.ACTIVE)) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 패키지를 등록하셨습니다. 삭제를 원하신다면 고객센터에 문의해주세요");
        }

        // 상품 정보 수정하기
        product.updateStatus(ProductStatus.REMOVE);

        // 테이블에서 삭제하기
        PackageToProductEntity packageToProduct = findByPackIdAndCpId(packageId, companyId);
        packageToProductRepository.delete(packageToProduct);
    }

    // 패키지 ID 와 업체 ID 로 매핑된 패키지 상품 찾기
    public PackageToProductEntity findByPackIdAndCpId(Long packId, Long cpId) {
        return packageToProductRepository.findByPackageEntity_PackIdAndCpId(packId, cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }
    // 활성화된 패키지 상품만 볼 수 있음
    public List<PackageEntity> findAllByActivePackage() {
        return packageRepository.findAll().stream()
                .filter(it -> it.getPackStatus().equals(PackageStatus.ACTIVE))
                .toList()
                ;
    }

    // 패키지에 등록된 모든 상품들의 작업 시간을 통일하기
    @Transactional
    private void updatePackageProductTaskTime(List<PackageToProductEntity> packageToProductEntityList, LocalTime taskTime) {
        for (PackageToProductEntity packageToProduct : packageToProductEntityList) {
            packageToProduct.getProduct().updateTaskTime(taskTime);
        }
    }
}
