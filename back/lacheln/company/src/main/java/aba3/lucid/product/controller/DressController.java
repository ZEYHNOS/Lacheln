package aba3.lucid.product.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.dto.DressResponse;
import aba3.lucid.product.business.DressBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/product/dress")
@RequiredArgsConstructor
@Tag(name = "Dress Controller", description = "드래스 관련 API")
public class DressController {

    private final DressBusiness dressBusiness;

    @PostMapping("/register")
    @Operation(summary = "드레스 등록", description = "새로운 드래스 상품을 등록")
    public API<DressResponse> registerDress(
            @Valid
            @RequestBody DressRequest req
    ) {
        // TODO 토큰을 통해 파싱한 업체 객체 데이터 가지고 오기

        DressResponse res = dressBusiness.registerProduct(1, req);
        log.debug("Register DressResponse : {}", res);

        return API.OK(res);
    }

    @PutMapping("/update/{productId}")
    @Operation(summary = "드래스 수정", description = "드래스 엔터티 수정")
    public API<DressResponse> updateDress(
            @PathVariable long productId,
            @Valid
            @RequestBody DressRequest request
    ) {
        // TODO 토큰을 통해 파싱한 업체 객체 데이터 가지고 오기

        DressResponse response = dressBusiness.updateProduct(1, productId, request);
        log.debug("Update DressResponse : {}", response);

        return API.OK(response);
    }


    @DeleteMapping("/delete/{productId}")
    @Operation(summary = "드래스 상품 삭제", description = "드래스 엔터티 삭제")
    public API<String> deleteDress(
            @PathVariable long productId
    ) {
        // TODO 토큰을 통해 파싱한 업체 객체 데이터 가지고 오기

        dressBusiness.deleteProduct(1, productId);
        return API.OK("상품이 삭제되었습니다.");
    }


    @GetMapping("/list/{companyId}")
    @Operation(summary = "드래스 상품 리스트", description = "삭제된 상품 제외한 모든 상품 보여주기")
    public API<List<DressResponse>> getDressList(
            @PathVariable long companyId
    ) {
        // TODO 요청한 업체의 리스트인지 확인해야함
        List<DressResponse> dressResponseList = dressBusiness.getValidProductList(companyId);
        log.debug("DressList dressResponseList : {}", dressResponseList);

        return API.OK(dressResponseList);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "드래스 상품 상세")
    public API<DressResponse> getDressDetailInfo(
            @PathVariable long productId
    ) {
        DressResponse dressResponse = dressBusiness.getProductDetailInfo(productId);

        return API.OK(dressResponse);
    }
}
