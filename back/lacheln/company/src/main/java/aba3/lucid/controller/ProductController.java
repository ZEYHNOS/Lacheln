package aba3.lucid.controller;

import aba3.lucid.business.ProductBusiness;
import aba3.lucid.common.api.API;
import aba3.lucid.dto.product.dress.DressRequest;
import aba3.lucid.dto.product.dress.DressResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductBusiness productBusiness;

    // 드레스 상품 등록
    @PostMapping("/dress/register")
    @Operation(summary = "드레스 상품 등록", description = "드레스 업체가 상품을 등록합니다.")
    public API<DressResponse> dressRegister(
            // TODO 업체 정보 가지고 오기
            @Valid
            DressRequest dressRequest
    ) {
        DressResponse res = productBusiness.dressRegister(dressRequest);
        return API.OK(res);
    }

    // 드레스 상품 수정
    @PostMapping("/dress/update/{id}")
    @Operation(summary = "드레스 상품 등록", description = "드레스 업체가 상품을 등록합니다.")
    public API<DressResponse> dressUpdate(
            // TODO 업체 정보 가지고 오기
            @Valid
            DressRequest dressRequest,
            @PathVariable long id
    ) {
        DressResponse res = productBusiness.update(id, dressRequest);
        return API.OK(res);
    }

    // 드레스 상품 등록
    @PostMapping("/register")
    @Operation(summary = "메이크업 상품 등록", description = "메이크업 업체가 상품을 등록합니다.")
    public API<String> makeUpRegister() {

        return null;
    }

    // 드레스 상품 등록
    @PostMapping("/register")
    @Operation(summary = "스튜디오 상품 등록", description = "스튜디오 업체가 상품을 등록합니다.")
    public API<String> studioRegister() {

        return null;
    }



}
