package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.ifs.ProductConverterIfs;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.status_code.ProductErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.dto.ProductRequest;
import aba3.lucid.domain.product.dto.ProductDetailResponse;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.product.service.ProductAbstractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public abstract class ProductAbstractBusiness<REQ extends ProductRequest, RES extends ProductDetailResponse, ENTITY extends ProductEntity>
        implements ProductBusinessIfs<REQ, RES, ENTITY> {

    private final ProductAbstractService<ENTITY, REQ> productService;
    private final ProductConverterIfs<ENTITY, REQ, RES> productConverterIfs;
    private final CompanyService companyService;

    // 상품 등록
    @Override
    public RES registerProduct(Long companyId, REQ req) {
        Validator.throwIfInvalidId(companyId);
        Validator.throwIfNull(req);

        // 요청을 보낸 업체가 해당 카테고리에 맞는지
        // Ex) 드래스 업체가 스튜디오 테이블에 저장하는 요청을 막는 용도
        CompanyEntity companyEntity = companyService.findByIdAndMatchCategoryWithThrow(companyId, getCategory());

        // DTO -> Entity
        ENTITY entity = productConverterIfs.toEntity(req, companyEntity);
        log.debug("hashTag : {} ", entity.getHashtagList());

        // Entity 저장
        ENTITY newEntity = productService.registerProduct(entity);
        log.debug("newEntity : {}", newEntity);

        // Entity -> DTO
        return productConverterIfs.toResponse(newEntity);
    }

    // 상품 업데이트
    @Override
    public RES updateProduct(Long companyId, Long productId, REQ req) {
        Validator.throwIfInvalidId(companyId, productId);
        Validator.throwIfNull(req);

        // 카테고리 검증
        companyService.findByIdAndMatchCategoryWithThrow(companyId, getCategory());

        // 요청을 보낸 업체의 상품인지
        ENTITY productEntity = productService.findByIdWithThrow(productId);
        productService.throwIfNotCompanyProduct(productEntity, companyId);

        // 상품 업데이트
        ENTITY updateEntity = productService.updateProduct(productEntity, req);

        // DTO -> Entity
        return productConverterIfs.toResponse(updateEntity);
    }

    // 상품 삭제(상태만 변환)
    @Override
    public void deleteProduct(Long companyId, Long productId) {
        Validator.throwIfInvalidId(companyId, productId);

        // 요청을 보낸 업체의 상품인지
        ENTITY productEntity = productService.findByIdWithThrow(productId);
        productService.throwIfNotCompanyProduct(productEntity, companyId);


        // 상품 삭제하기(상태만 변경)
        productService.deleteProduct(productEntity);
    }

    // 유저용
    // 활성화된 상품 리스트만 출력(패키지, 비공개, 삭제 제외)
    public List<RES> getActiveProductList(Long companyId) {
        return productService.getActiveProductList(companyId).stream()
                .map(productConverterIfs::toResponse)
                .toList()
                ;
    }

    // 현재 영역의 카테고리 반환(DressBusiness 면 CompanyCategory.D 반환)
    public abstract CompanyCategory getCategory();

    // 상품 상세 정보
    public RES getProductDetailInfo(Long productId) {
        Validator.throwIfInvalidId(productId);

        ENTITY existingProduct = productService.findByIdWithThrow(productId);

        // 삭제된 상품일 때
        if (existingProduct.getPdStatus() == ProductStatus.REMOVE) {
            throw new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND);
        }

        return productConverterIfs.toResponse(existingProduct);
    }
}
