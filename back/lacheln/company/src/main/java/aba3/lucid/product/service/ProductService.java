package aba3.lucid.product.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.status_code.ProductErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.dto.ProductSnapshot;
import aba3.lucid.domain.product.dto.option.OptionSnapshot;
import aba3.lucid.domain.product.entity.OptionDetailEntity;
import aba3.lucid.domain.product.entity.OptionEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 특정 업체 상품 리스트
    public List<ProductEntity> getCompanyProductList(Long companyId, ProductStatus status) {
        log.info("CompanyProductList : {}", companyId);
        List<ProductEntity> productEntityList = productRepository.findAllByCompany_CpIdAndPdStatusNot(companyId, ProductStatus.REMOVE);
        if (status == null) {
            return productEntityList;
        }

        return productEntityList.stream()
                .filter(it -> it.getPdStatus().equals(status))
                .toList()
                ;
    }

    // todo 검색어 만들기
    public List<ProductEntity> getProductList(CompanyCategory category, int minimum, int maximum, boolean isDesc) {
        Sort sort = isDesc ? Sort.by(Sort.Order.desc("id")) : Sort.by(Sort.Order.asc("id"));

        return List.of();
    }

    // 해시 태그로 상품 찾기
    public List<ProductEntity> findByHashtag(String tagName) {
        return productRepository.findAllByHashtagList_TagName(tagName);
    }

    // 삭제 상태인 상품 1달 뒤 삭제
    public void removeData() {
        // 삭제한 지 오늘로부터 1달 전인 데이터 삭제하기
        List<ProductEntity> removeProductEntityList = productRepository.findAllByDeleteDateBefore(LocalDateTime.now().minusMonths(1));

        log.info("Remove Product Entity List : {}", removeProductEntityList);
        productRepository.deleteAll(removeProductEntityList);
    }



    public void updateStatusToPackage(ProductEntity product) {
        product.setStatusToPackage();

        productRepository.save(product);
    }

    // 상품 스냅샷 확인 로직
    public void verifySnapshotMatch(ProductSnapshot snapshot) {
        Validator.throwIfNull(snapshot);

        ProductEntity product = findByIdWithThrow(snapshot.getId());
        // 1. 기본적인 상품 정보 확인
        if (!product.getPdName().equals(snapshot.getName()) || // 이름
            product.getPdPrice().compareTo(snapshot.getPrice()) != 0 || // 가격
            product.getPdTaskTime() != snapshot.getTaskTime()) { // 작업 시간
            throw new ApiException(ProductErrorCode.PRODUCT_SNAPSHOT_MISMATCH);
        }

        // 2. 옵션 정보 확인
        Map<Long, OptionEntity> optionEntityMap = product.getOpList().stream()
                .collect(Collectors.toMap(OptionEntity::getOpId, it -> it));
        for (OptionSnapshot optionSnapshot : snapshot.getOptionSnapshotList()) {
            OptionEntity option = optionEntityMap.getOrDefault(optionSnapshot.getOptionId(), null);

            if (option == null) {
                throw new ApiException(ProductErrorCode.PRODUCT_SNAPSHOT_MISMATCH);
            }

            // 옵션 상세 비교하기
            Map<Long, OptionDetailEntity> optionDetailEntityMap = option.getOpDtList().stream()
                    .collect(Collectors.toMap(OptionDetailEntity::getOpDtId, it -> it));

            if (!optionDetailEntityMap.containsKey(optionSnapshot.getOptionDetailId())) {
                throw new ApiException(ProductErrorCode.PRODUCT_SNAPSHOT_MISMATCH);
            }
        }
    }

    // 상품 상태 변경하기
    public ProductEntity updateStatus(CompanyEntity company, ProductEntity product, ProductStatus status) {
        throwIfNotOwnProduct(product, company.getCpId());
        product.setStatus(status);

        return productRepository.save(product);
    }

    // 상품 찾기
    public ProductEntity findByIdWithThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

    public List<ProductEntity> findAllById(List<Long> productIdList) {
        return productRepository.findAllById(productIdList);
    }

    // 해당 상품의 소유 업체인지
    public void throwIfNotOwnProduct(ProductEntity product, Long companyId) {
        if (product.getCompany().getCpId() != companyId) {
            throw new ApiException(ProductErrorCode.NO_PRODUCT_OWNERSHIP);
        }
    }
}
