package aba3.lucid.packages.service;

import aba3.lucid.alert.service.CompanyAlertService;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.status_code.PackageErrorCode;
import aba3.lucid.common.status_code.ProductErrorCode;
import aba3.lucid.domain.alert.dto.CompanyAlertDto;
import aba3.lucid.domain.packages.dto.PackageResponse;
import aba3.lucid.domain.packages.dto.PackageUpdateRequest;
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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PackageService {

    private final ProductService productService;
    private final CompanyAlertService companyAlertService;

    private final PackageRepository packageRepository;
    private final PackageToProductRepository packageToProductRepository;

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

    // 패키지 정보 수정
    @Transactional
    public PackageEntity packageUpdate(PackageEntity packageEntity, PackageUpdateRequest request, Long adminId) {
        // 방장의 요청이 아닐 때
        throwIfNotAdminRequest(packageEntity, adminId);

        // 정보 변경(상태는 변경 X)
        packageEntity.updateAdditionalField(request);

        // 상태 변경
        if (packageEntity.getPackStatus() != request.getStatus()) {
            switch (request.getStatus()) {
                // 활성화 하기 전 모든 정보가 들어있는지
                case PUBLIC :
                    throwIfImpossibleToPublicChange(packageEntity);
                    break;
                case REMOVE:
                    // todo 삭제하기 전 예약 현황 확인하기
                    break;
                case PRIVATE:
                    // todo PUBLIC 일 때 막기
                    break;
            }

            // 패키지 Entity 상태 Update
            packageEntity.updatePackageStatus(request.getStatus());
        }


        return packageRepository.save(packageEntity);
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
        return packageToProductRepository.existsByPackageEntityAndProduct(packageEntity, productEntity);
    }


    // 상품 등록 가능 여부 확인
    protected void throwIfImpossibleToPublicChange(PackageEntity entity) {
        // 비공개 상태여야 함
        if (entity.getPackStatus() != PackageStatus.PRIVATE) {
            throw new ApiException(PackageErrorCode.INVALID_PACKAGE_REGISTRATION, "비공개 상태여야합니다.");
        }

        // 모든 상품이 등록되어야 한다.
        if (countDistinctProductsByPackage(entity.getPackId()) != 3) {
            throw new ApiException(PackageErrorCode.INVALID_PACKAGE_REGISTRATION);
        }

        // 패키지 종료일이 등록되지 않았거나 오늘보다 하루 이상이지 않을 때
        LocalDateTime minimumDateTime = LocalDate.now().plusDays(1).atStartOfDay();
        if (entity.getPackEndDate().isBefore(minimumDateTime)) {
            throw new ApiException(PackageErrorCode.INVALID_PACKAGE_END_DATE);
        }
    }

    // 현재 패키지에 등록된 상품 갯수
    public long countDistinctProductsByPackage(Long packageId) {
        return packageToProductRepository.countDistinctProductsByPackage(packageId);
    }


    // 패키지 업로드
    public PackageEntity packageUpload(PackageEntity packageEntity, Long adminId) {
        // 방장이 아닐 때
        throwIfNotAdminRequest(packageEntity, adminId);

        // 모든 상품이 등록되었는지
        if (countDistinctProductsByPackage(packageEntity.getPackId()) != 3) {
            throw new ApiException(PackageErrorCode.INVALID_PACKAGE_REGISTRATION);
        }

        // 패키지가 삭제되었을 때
        if (packageEntity.getPackStatus().equals(PackageStatus.REMOVE)) {
            throw new ApiException(PackageErrorCode.PACKAGE_NOT_FOUND);
        }

        // 패키지가 이미 공개 상태일 때
        if (packageEntity.getPackStatus().equals(PackageStatus.PUBLIC)) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 공개 상태입니다.");
        }

        // todo 모든 패키지 정보가 잘 저장되었는지

        // 상태 변경 후 저장
        packageEntity.updatePackageStatus(PackageStatus.PUBLIC);
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

    public void responseInsertPrice(List<PackageResponse> packageResponseList) {
        packageResponseList.forEach(it -> {
            it.setTotalPrice(getTotalPrice(it.getId()));
        });
    }

    public BigInteger getTotalPrice(Long packageId) {
        List<PackageToProductEntity> packageToProductEntityList = packageToProductRepository.findAllByPackageEntity_packId(packageId);

        BigInteger result = BigInteger.ZERO;
        for (PackageToProductEntity entity : packageToProductEntityList) {
            result = result.add(entity.getProduct().getPdPrice());
        }

        return result;
    }
}
