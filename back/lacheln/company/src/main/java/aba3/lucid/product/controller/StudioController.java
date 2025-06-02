package aba3.lucid.product.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.product.studio.dto.StudioRequest;
import aba3.lucid.domain.product.studio.dto.StudioResponse;
import aba3.lucid.product.business.StudioBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/product/studio")
@RequiredArgsConstructor
@Tag(name = "Studio Controller", description = "스튜디오 관련 API")
public class StudioController {

    private final StudioBusiness studioBusiness;

    @PostMapping("/register")
    @Operation(summary = "스튜디오 등록", description = "새로운 스튜디오 상품을 등록")
    public API<StudioResponse> registerStudio(
//            @Valid
            @RequestBody StudioRequest req,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        StudioResponse res = studioBusiness.registerProduct(customUserDetails.getCompanyId(), req);
//        StudioResponse res = studioBusiness.registerProduct(2L, req);
        log.debug("Register StudioResponse : {}", res);

        return API.OK(res);
    }

    @PutMapping("/update/{productId}")
    @Operation(summary = "스튜디오 수정", description = "스튜디오 엔터티 수정")
    public API<StudioResponse> updateStudio(
            @PathVariable Long productId,
//            @Valid
            @RequestBody StudioRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        StudioResponse response = studioBusiness.updateProduct(customUserDetails.getCompanyId(), productId, request);
//        StudioResponse response = studioBusiness.updateProduct(2L, productId, request);
        log.debug("Update StudioResponse : {}", response);

        return API.OK(response);
    }


    @DeleteMapping("/delete/{productId}")
    @Operation(summary = "스튜디오 상품 삭제", description = "스튜디오 엔터티 삭제")
    public API<String> deleteStudio(
            @PathVariable Long productId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        studioBusiness.deleteProduct(customUserDetails.getCompanyId(), productId);
//        studioBusiness.deleteProduct(2L, productId);
        return API.OK("상품이 삭제되었습니다.");
    }


    @GetMapping("/{productId}")
    @Operation(summary = "스튜디오 상품 상세")
    public API<StudioResponse> getStudioDetailInfo(
            @PathVariable Long productId
    ) {
        StudioResponse studioResponse = studioBusiness.getProductDetailInfo(productId);

        return API.OK(studioResponse);
    }

}
