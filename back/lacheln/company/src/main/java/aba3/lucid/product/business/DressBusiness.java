package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.dress.convertor.DressConverter;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.dto.DressResponse;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.product.service.DressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class DressBusiness implements ProductBusinessIfs<DressRequest, DressResponse> {

    private final DressService dressService;
    private final CompanyService companyService;

    private final DressConverter dressConverter;

    // Dress Register
    @Override
    public DressResponse registerProduct(long companyId, DressRequest dressRequest) {
        if (dressRequest == null || Validator.isInvalidId(companyId)) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER, "DressRequest 값을 받지 못했습니다.");
        }
        // TODO 요청을 보낸 업체와 companyId 같은지 확인

        CompanyEntity companyEntity = companyService.findByIdAndMatchCategoryWithThrow(companyId, CompanyCategory.D);

        // request -> entity
        log.debug("DressRequest : {}", dressRequest);
        DressEntity dressEntity = dressConverter.toEntity(dressRequest, companyEntity);
        log.debug("DressEntity : {}", dressEntity);

        // 드레스 저장
        DressEntity newDressEntity = dressService.registerProduct(dressEntity);
        log.debug("newDressEntity : {}", newDressEntity);

        return dressConverter.toResponse(newDressEntity);
    }

    // Dress Update
    @Override
    public DressResponse updateProduct(long companyId, long dressId, DressRequest dressRequest) {
        if (dressRequest == null || Validator.isInvalidId(companyId, dressId)) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER, "DressRequest 값을 받지 못했습니다.");
        }

        // 업체 id를 통해 업체가 존재 유무 + 카테고리가 드래스인지
        CompanyEntity companyEntity = companyService.findByIdAndMatchCategoryWithThrow(companyId, CompanyCategory.D);

        // 상품 id를 통해 드레스가 존재 유무 + 드래스 객체 가지고 오기
        DressEntity existingDress = dressService.findByIdWithThrow(dressId);

        // 해당 상품이 요청한 업체의 상품인지 확인
        if (existingDress.getCompany().getCpId() != companyId) {
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }

        // 업데이트
        DressEntity updateDress = dressService.updateProduct(existingDress, dressRequest);

        // entity -> dto 및 반환
        return dressConverter.toResponse(updateDress);
    }

    @Override
    public void deleteProduct(long companyId, long productId) {
        // 파라미터 유효성 검사
        if (Validator.isInvalidId(companyId, productId)) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER);
        }

        // 해당 업체의 상품인지 확인하기
        DressEntity entity = dressService.findByIdWithThrow(productId);
        if (entity.getCompany().getCpId() != companyId) {
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }

        // 삭제
        dressService.deleteProduct(entity);
    }

    @Override
    public List<DressResponse> getProductList(long companyId) {
        // 파라미터 유효성 검사
        if (Validator.isInvalidId(companyId)) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER);
        }

        // Entity -> Dto
        List<DressResponse> dressResponseList = dressService.getProductList(companyId).stream()
                .map(dressConverter::toResponse)
                .toList();

        log.debug("DressResponseList : {}", dressResponseList);
        return dressResponseList;
    }
}
