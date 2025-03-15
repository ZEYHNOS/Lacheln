package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
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
