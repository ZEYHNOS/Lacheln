package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.ifs.ProductConverterIfs;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.dto.ProductRequest;
import aba3.lucid.domain.product.dto.ProductResponse;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public abstract class ProductBusiness<REQ extends ProductRequest, RES extends ProductResponse, ENTITY extends ProductEntity>
        implements ProductBusinessIfs<REQ, RES, ENTITY> {

    private final ProductService<ENTITY, REQ> productService;
    private final ProductConverterIfs<ENTITY, REQ, RES> productConverterIfs;
    private final CompanyService companyService;

    // 상품 등록
    @Override
    public RES registerProduct(long companyId, REQ req) {
        // 매개변수 유효성 검사
        Validator.throwIfInvalidId(companyId);
        Validator.throwIfNull(req);

        // 요청을 보낸 업체가 해당 카테고리에 맞는지
        // Ex) 드래스 업체가 스튜디오 테이블에 저장하는 요청을 막는 용도
        log.debug("Request HashTag {} ", req.getHashTagList());
        CompanyEntity companyEntity = companyService.findByIdAndMatchCategoryWithThrow(companyId, getCategory());
        log.debug("Request {}", req);

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
    public RES updateProduct(long companyId, long productId, REQ req) {
        // 매개변수 유효성 검사
        Validator.throwIfInvalidId(companyId, productId);
        Validator.throwIfNull(req);

        // 카테고리 검증
        companyService.findByIdAndMatchCategoryWithThrow(companyId, getCategory());

        // 요청을 보낸 업체의 상품인지
        ENTITY productEntity = productService.findByIdWithThrow(productId);
        productService.throwIfNotCompanyProduct(productEntity, companyId);

        // 가격 수정 금지
        throwIfNotEquals(productEntity.getPdPrice(), req.getPrice());

        // 상품 업데이트
        ENTITY updateEntity = productService.updateProduct(productEntity, req);

        // DTO -> Entity
        return productConverterIfs.toResponse(updateEntity);
    }

    // 상품 삭제(상태만 변환)
    @Override
    public void deleteProduct(long companyId, long productId) {
        // 유효성 검사
        Validator.throwIfInvalidId(companyId, productId);

        // 요청을 보낸 업체의 상품인지
        ENTITY productEntity = productService.findByIdWithThrow(productId);
        productService.throwIfNotCompanyProduct(productEntity, companyId);


        // 상품 삭제하기(상태만 변경)
        productService.deleteProduct(productEntity);
    }

    // 유저용
    // 활성화된 상품 리스트만 출력(패키지, 비공개, 삭제 제외)
    public List<RES> getActiveProductList(long companyId) {
        return productService.getActiveProductList(companyId).stream()
                .map(productConverterIfs::toResponse)
                .toList()
                ;
    }

    // 업체용
    // 삭제 된 상품을 제외한 모든 상품 리스트 출력
    public List<RES> getValidProductList(long companyId) {

        companyService.findByIdAndMatchCategoryWithThrow(companyId, getCategory());

        return productService.getValidProductList(companyId).stream()
                .map(productConverterIfs::toResponse)
                .toList()
                ;
    }

    public void throwIfNotEquals(BigInteger n1, BigInteger n2) {
        // BigInteger 값 비교할 때 작으면 -1, 같으면 0, 더 크면 1
        if (n1.compareTo(n2) != 0) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "상품 가격은 수정할 수 없습니다.");
        }
    }

    // 현재 영역의 카테고리 반환(DressBusiness 면 CompanyCategory.D 반환)
    public abstract CompanyCategory getCategory();

    public RES getProductDetailInfo(long productId) {
        Validator.throwIfInvalidId(productId);

        ENTITY existingProduct = productService.findByIdWithThrow(productId);

        return productConverterIfs.toResponse(existingProduct);
    }
}
