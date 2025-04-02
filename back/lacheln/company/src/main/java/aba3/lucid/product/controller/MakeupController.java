package aba3.lucid.product.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.product.dto.option.ProductResponse;
import aba3.lucid.domain.product.makeup.dto.MakeUpResponse;
import aba3.lucid.domain.product.makeup.dto.MakeupRequest;
import aba3.lucid.product.business.MakeupBusiness;
import aba3.lucid.product.business.ProductBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @Valid
            @RequestBody MakeupRequest req
    ) {
        MakeUpResponse res = makeupBusiness.registerProduct(AuthUtil.getCompanyId(), req);
        log.debug("Register MakeupResponse : {}", res);

        return API.OK(res);
    }

    @PutMapping("/update/{productId}")
    @Operation(summary = "메이크업 수정", description = "메이크업 엔터티 수정")
    public API<MakeUpResponse> updateMakeup(
            @PathVariable Long productId,
            @Valid
            @RequestBody MakeupRequest request
    ) {
        MakeUpResponse response = makeupBusiness.updateProduct(AuthUtil.getCompanyId(), productId, request);
        log.debug("Update MakeupResponse : {}", response);

        return API.OK(response);
    }

    @DeleteMapping("/delete/{productId}")
    @Operation(summary = "메이크업 상품 삭제", description = "메이크업 엔터티 삭제")
    public API<String> deleteMakeup(
            @PathVariable Long productId
    ) {
        makeupBusiness.deleteProduct(AuthUtil.getCompanyId(), productId);
        return API.OK("상품이 삭제되었습니다.");
    }

    @GetMapping("/{productId}")
    @Operation(summary = "메이크업 상품 상세")
    public API<MakeUpResponse> getStudioDetailInfo(
            @PathVariable Long productId
    ) {
        MakeUpResponse makeUpResponse = makeupBusiness.getProductDetailInfo(AuthUtil.getCompanyId(), productId);

        return API.OK(makeUpResponse);
    }
}
