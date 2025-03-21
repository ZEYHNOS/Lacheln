package aba3.lucid.packages.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.packages.entity.PackageToCompanyEntity;
import aba3.lucid.domain.packages.entity.PackageToProductEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.repository.PackageRepository;
import aba3.lucid.domain.product.repository.PackageToCompanyRepository;
import aba3.lucid.domain.product.repository.PackageToProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PackageService {

    /**
     * 1. 업체(방장)이 패키지 생성하기 (완료)
     *    - 패키지 명, 패키지 설명 데이터만 받고 생성하기
     * 2. 업체(방장)이 다른 업체 2개를 추가하기
     *    - 모두 다른 업체인지(카테고리 포함) 확인하기
     * 3. 모든 업체가 하나의 상품을 등록하기
     *    - 이미 상품을 등록해놓은 상태여야한다.
     * 4. 업체가 상품을 등록하면 승인 여부를 선택할 수 있게 된다
     *    - 업체(방장)는 방장 제외 다른 업체가 모두 승인하기 전까지 승인을 누를 수 없다
     *    - 다른 업체들이 모두 승인 버튼을 누르고 할인율, 개시일, 종료일, 대표이미지가 모두 등록되어있어야 승인을 누를 수 있다
     * 5. 방장이 승인 버튼을 누르면 그 순간부터 해당 상품은 수정, 삭제를 할 수 없다.
     */

    private final PackageRepository packageRepository;
    private final PackageToCompanyRepository packToCpRepository;
    private final PackageToProductRepository packageToProductRepository;


    // 패키지 생성
    public PackageEntity packageRegister(PackageEntity entity) {
        return packageRepository.save(entity);
    }

    // 패키지 업체 초대
    public PackageToCompanyEntity invitationPackageGroup(PackageToCompanyEntity entity) {
        return packToCpRepository.save(entity);
    }


    // 패키지 상품 등록



    // 패키지 승인 요청


    // 패키지 수정


    // 패키지 삭제



    // 패키지 Entity 가지고 오기
    public PackageEntity findPackageEntityById(long packageId) {
        return packageRepository.findById(packageId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 패키지는 존재하지 않습니다."));
    }

    // 패키지에 속한 업체 수
    public long countByPackageInCompany(long packageId) {
        return packToCpRepository.countByPackageEntity_PackId(packageId);
    }

    public PackageToCompanyEntity findByPackageEntityAndCompanyWithThrow(PackageEntity packageEntity, CompanyEntity companyEntity) {
        return packToCpRepository.findByPackageEntityAndCompany(packageEntity, companyEntity)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }

    public PackageToProductEntity registerProduct(PackageToProductEntity packageToProductEntity) {
        return packageToProductRepository.save(packageToProductEntity);
    }

    public boolean existsByPackageAndProduct(PackageEntity packageEntity, ProductEntity product) {
        return packageToProductRepository.existsByPackageEntityAndProduct(packageEntity, product);
    }


    // 모든 패키지 리스트 출력
    public List<PackageEntity> findAllPackageList() {
        return packageRepository.findAll();
    }
}
