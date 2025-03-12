package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.dress.convertor.DressConverter;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.dto.DressResponse;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.product.service.CompanyService;
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
        if (dressRequest == null || companyId < 0) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "DressRequest 값을 받지 못했습니다.");
        }

        // 업체 검수(존재 유무, 카테고리)
        if (!companyService.matchCategory(companyId, CompanyCategory.D)) {
            // 업체가 없거나 드레스 카테고리가 아니라면 Error
            throw new ApiException(ErrorCode.BAD_REQUEST, "드레스 업체가 아니거나 업체가 존재하지 않습니다.");
        }

        DressEntity dressEntity = dressConverter.toEntity(dressRequest);
        DressEntity newDressEntity = dressService.registerProduct(dressEntity);

        return dressConverter.toResponse(newDressEntity);
    }

    @Override
    public DressResponse updateProduct(long companyId, DressRequest dressRequest) {
        return null;
    }

    @Override
    public void deleteProduct(long companyId) {

    }

    @Override
    public List<DressResponse> getProductList(DressRequest dressRequest) {
        return List.of();
    }
}
