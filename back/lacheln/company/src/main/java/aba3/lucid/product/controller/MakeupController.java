package aba3.lucid.product.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.product.makeup.dto.MakeUpResponse;
import aba3.lucid.domain.product.makeup.dto.MakeupRequest;
import aba3.lucid.product.business.MakeupBusiness;
import aba3.lucid.product.business.ProductBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/product/makeup")
@RequiredArgsConstructor
@Tag(name = "Makeup Controller", description = "메이크업 관련 API")
public class MakeupController {

    private final MakeupBusiness makeupBusiness;
    private final ProductBusiness productBusiness;

    @PostMapping("/register")
    @Operation(summary = "메이크업 등록", description = "새로운 메이크업 상품을 등록")
    public API<MakeUpResponse> registerMakeup(
            @RequestBody MakeupRequest req,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        MakeUpResponse res = makeupBusiness.registerProduct(customUserDetails.getCompanyId(), req);
//        MakeUpResponse res = makeupBusiness.registerProduct(3L, req);
        log.debug("Register MakeupResponse : {}", res);

        return API.OK(res);
    }

    @PutMapping("/update/{productId}")
    @Operation(summary = "메이크업 수정", description = "메이크업 엔터티 수정")
    public API<MakeUpResponse> updateMakeup(
            @PathVariable Long productId,
            @RequestBody MakeupRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        MakeUpResponse response = makeupBusiness.updateProduct(customUserDetails.getCompanyId(), productId, request);
//        MakeUpResponse response = makeupBusiness.updateProduct(3L, productId, request);
        log.debug("Update MakeupResponse : {}", response);

        return API.OK(response);
    }

    @DeleteMapping("/delete/{productId}")
    @Operation(summary = "메이크업 상품 삭제", description = "메이크업 엔터티 삭제")
    public API<String> deleteMakeup(
            @PathVariable Long productId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        makeupBusiness.deleteProduct(customUserDetails.getCompanyId(), productId);
        makeupBusiness.deleteProduct(3L, productId);
        return API.OK("상품이 삭제되었습니다.");
    }

    @GetMapping("/{productId}")
    @Operation(summary = "메이크업 상품 상세")
    public API<MakeUpResponse> getStudioDetailInfo(
            @PathVariable Long productId
    ) {
        MakeUpResponse makeUpResponse = makeupBusiness.getProductDetailInfo(productId);

        return API.OK(makeUpResponse);
    }
}
