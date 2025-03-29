package aba3.lucid.product.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.status_code.ProductErrorCode;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 특정 업체 상품 리스트
    public List<ProductEntity> getCompanyProductList(Long companyId) {
        log.info("CompanyProductList : {}", companyId);
        List<ProductEntity> productEntityList = productRepository.findAllByCompany_CpId(companyId);

        log.info("CompanyProductList : {}", productEntityList);
        return productEntityList;
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

    // 상품 찾기
    public ProductEntity findByIdWithThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

    public void updateStatusToPackage(ProductEntity product) {
        product.updateStatusToPackage();

        productRepository.save(product);
    }
}
