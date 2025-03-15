package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.company.business.CompanyService;
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

    @Override
    public DressResponse registerProduct(long companyId, DressRequest dressRequest) {
        if (dressRequest == null || companyId <= 0) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER, "DressRequest 값을 받지 못했습니다.");
        }
        // TODO 요청을 보낸 업체와 companyId 같은지 확인

        CompanyEntity companyEntity = companyService.findByIdAndMatchCategoryWithThrow(companyId, CompanyCategory.D);

        // request -> entity
        log.info("DressRequest : {}", dressRequest);
        DressEntity dressEntity = dressConverter.toEntity(dressRequest, companyEntity);
        log.info("DressEntity : {}", dressEntity);

        // 드레스 저장
        DressEntity newDressEntity = dressService.registerProduct(dressEntity);
        log.info("newDressEntity : {}", newDressEntity);

        return dressConverter.toResponse(newDressEntity);
    }

    @Override
    public DressResponse updateProduct(long companyId, long dressId, DressRequest dressRequest) {
        if (dressRequest == null || companyId <= 0) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER, "DressRequest 값을 받지 못했습니다.");
        }

        // TODO 요청을 보낸 업체와 companyId 같은지 확인

        // 업체 id를 통해 업체가 존재 유무 + 카테고리가 드래스인지
        CompanyEntity companyEntity = companyService.findByIdAndMatchCategoryWithThrow(companyId, CompanyCategory.D);

        // 상품 id를 통해 드레스가 존재 유무 + 드래스 객체 가지고 오기
        DressEntity existingDress = dressService.findByIdWithThrow(dressId);

        // 업데이트
        // TODO 업데이트 로직 만들기
        DressEntity updateDress = dressService.updateProduct(existingDress, dressRequest);

        return dressConverter.toResponse(updateDress);
    }

    @Override
    public void deleteProduct(long companyId, long productId) {

    }

    @Override
    public List<DressResponse> getProductList(DressRequest dressRequest) {
        return List.of();
    }
}
