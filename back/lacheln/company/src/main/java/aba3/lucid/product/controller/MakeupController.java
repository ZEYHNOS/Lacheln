package aba3.lucid.product.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.product.makeup.dto.MakeUpResponse;
import aba3.lucid.domain.product.makeup.dto.MakeupRequest;
import aba3.lucid.product.business.MakeupBusiness;
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

    @PostMapping("/register")
    @Operation(summary = "메이크업 등록", description = "새로운 메이크업 상품을 등록")
    public API<MakeUpResponse> registerMakeup(
            @Valid
            @RequestBody MakeupRequest req
    ) {
        // TODO 토큰을 통해 파싱한 업체 객체 데이터 가지고 오기

        MakeUpResponse res = makeupBusiness.registerProduct(3, req);
        log.debug("Register MakeupResponse : {}", res);

        return API.OK(res);
    }

    @PutMapping("/update/{productId}")
    @Operation(summary = "메이크업 수정", description = "메이크업 엔터티 수정")
    public API<MakeUpResponse> updateMakeup(
            @PathVariable long productId,
            @Valid
            @RequestBody MakeupRequest request
    ) {
        // TODO 토큰을 통해 파싱한 업체 객체 데이터 가지고 오기

        MakeUpResponse response = makeupBusiness.updateProduct(3, productId, request);
        log.debug("Update MakeupResponse : {}", response);

        return API.OK(response);
    }

    @DeleteMapping("/delete/{productId}")
    @Operation(summary = "메이크업 상품 삭제", description = "메이크업 엔터티 삭제")
    public API<String> deleteMakeup(
            @PathVariable long productId
    ) {
        // TODO 토큰을 통해 파싱한 업체 객체 데이터 가지고 오기

        makeupBusiness.deleteProduct(1, productId);
        return API.OK("상품이 삭제되었습니다.");
    }

    @GetMapping("/list/{companyId}")
    @Operation(summary = "메이크업 상품 리스트", description = "삭제된 상품 제외한 모든 상품 보여주기")
    public API<List<MakeUpResponse>> getMakeupList(
            @PathVariable long companyId
    ) {
        // TODO 요청한 업체의 리스트인지 확인해야함
        List<MakeUpResponse> makeupResponseList = makeupBusiness.getValidProductList(companyId);
        log.debug("MakeupList makeupResponseList : {}", makeupResponseList);

        return API.OK(makeupResponseList);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "메이크업 상품 상세")
    public API<MakeUpResponse> getStudioDetailInfo(
            @PathVariable long productId
    ) {
        MakeUpResponse makeUpResponse = makeupBusiness.getProductDetailInfo(productId);

        return API.OK(makeUpResponse);
    }
}
