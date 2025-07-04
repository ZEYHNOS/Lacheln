package aba3.lucid.product.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.image.RepresentativeImage;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.status_code.ProductErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.payment.dto.PopularDto;
import aba3.lucid.domain.product.dto.PopularResponse;
import aba3.lucid.domain.product.dto.ProductSearchRecord;
import aba3.lucid.domain.product.dto.ProductSnapshot;
import aba3.lucid.domain.product.dto.option.OptionSnapshot;
import aba3.lucid.domain.product.entity.OptionDetailEntity;
import aba3.lucid.domain.product.entity.OptionEntity;
import aba3.lucid.domain.product.entity.PopularEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.enums.ProductSortBy;
import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.domain.product.repository.OptionRepository;
import aba3.lucid.domain.product.repository.PopularRepository;
import aba3.lucid.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final PopularRepository popularRepository;
    private final OptionRepository optionRepository;

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
        product.updateStatusToPackage();

        productRepository.save(product);
    }

    public void verifySnapshotListMatch(List<ProductSnapshot> productSnapshotList) {
        productSnapshotList.forEach(this::verifySnapshotMatch);
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
        product.updateStatus(status);

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

    public Page<ProductEntity> getProductPage(Pageable pageable, ProductSearchRecord productSearchRecord) {
        Sort.Direction direction = productSearchRecord.isAsc() == null || productSearchRecord.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortBy = productSearchRecord.orderBy() == null ? ProductSortBy.CREATE_AT.getSortBy() : productSearchRecord.orderBy();


        return productRepository.searchProductPage(
                pageable,
                productSearchRecord.productName(),
                productSearchRecord.companyName(),
                productSearchRecord.category(),
                productSearchRecord.maximum(),
                productSearchRecord.minimum()
        );
    }

    // 인기 상품 랭킹 생성
    @Transactional
    public void createPopularProduct(List<PopularEntity> list) {
        popularRepository.deleteAll();
        log.info("popularEntityList : {}", list);
        popularRepository.saveAll(list);
    }

    public List<PopularResponse> getPopularProductList() {
        List<PopularEntity> popularEntityList = popularRepository.findAll();
        List<Long> productIdList = popularEntityList.stream().map(PopularEntity::getProductId).toList();
        List<ProductEntity> productEntityList = productRepository.findAllById(productIdList);

        Map<Long, PopularResponse> map = new HashMap<>();
        for (ProductEntity product : productEntityList) {
            map.put(product.getPdId(), PopularResponse.builder()
                            .price(product.getPdPrice())
                            .productImageUrl(RepresentativeImage.getRepresentativeImage(product.getImageList()))
                            .productName(product.getPdName())
                            .productId(product.getPdId())
                            .companyId(product.getCompany().getCpId())
                            .companyName(product.getCompany().getCpName())
                            .category(product.getCompany().getCpCategory())
                    .build());
        }

        List<PopularResponse> result = new ArrayList<>();
        for (PopularEntity popular : popularEntityList) {
            PopularResponse response = map.get(popular.getProductId());
            response.setRank(popular.getPopularRank());
            result.add(response);
        }

        return result;
    }

    @Transactional
    public void deleteOption(CompanyEntity company, OptionEntity option) {
        if (!company.equals(option.getProduct().getCompany())) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        optionRepository.delete(option);
    }

    public OptionEntity findByOptionIdWithThrow(Long opId) {
        return optionRepository.findById(opId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }
}
