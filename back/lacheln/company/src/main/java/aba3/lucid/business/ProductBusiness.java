package aba3.lucid.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.converter.product.DressConverter;
import aba3.lucid.domain.product.DressEntity;
import aba3.lucid.dto.product.dress.DressRequest;
import aba3.lucid.dto.product.dress.DressResponse;
import aba3.lucid.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Business
@AllArgsConstructor
public class ProductBusiness {

    private final ProductService productService;

    private final DressConverter dressConverter;


    // 드레스 등록하기
    public DressResponse dressRegister(DressRequest dressRequest) {
        // TODO 업체 정보 매개변수로 받고 확인하기

        if (dressRequest == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "드레스 req Null");
        }

        DressEntity entity = dressConverter.toEntity(dressRequest);

        DressEntity newEntity = productService.dressRegister(entity);
        // TODO 예외 처리

        DressResponse res = dressConverter.toResponse(newEntity);
        log.info(res.toString());

        return res;
    }
}
