package aba3.lucid.controller;

import aba3.lucid.business.ProductBusiness;
import aba3.lucid.common.api.API;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.dto.DressResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 드레스 상품 삭제
    @DeleteMapping("/dress/delete/{id}")
    @Operation(summary = "드레스 상품 삭제", description = "드레스 업체가 상품을 삭제합니다.")
    public API<String> dressDelete(
            @PathVariable long id
    ) {
        // TODO 업체 정보 가지고 오기


        productBusiness.dressProductDelete(id);

        return API.OK("삭제되었습니다.");
    }

    // 드레스 상품 보기
    @GetMapping("/dress/list")
    @Operation(summary = "드레스 상품 리스트", description = "드레스 업체의 상품 리스트를 볼 수 있습니다.")
    public API<List<DressResponse>> getDressList() {
        List<DressResponse> dressResponseList = productBusiness.getDressList();

        return API.OK(dressResponseList);
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
