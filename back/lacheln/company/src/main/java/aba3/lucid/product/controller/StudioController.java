package aba3.lucid.product.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.product.studio.dto.StudioRequest;
import aba3.lucid.domain.product.studio.dto.StudioResponse;
import aba3.lucid.product.business.StudioBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @Valid
            @RequestBody StudioRequest req
    ) {
        // TODO 토큰을 통해 파싱한 업체 객체 데이터 가지고 오기

        StudioResponse res = studioBusiness.registerProduct(2, req);
        log.debug("Register StudioResponse : {}", res);

        return API.OK(res);
    }

    @PutMapping("/update/{productId}")
    @Operation(summary = "스튜디오 수정", description = "스튜디오 엔터티 수정")
    public API<StudioResponse> updateStudio(
            @PathVariable long productId,
            @Valid
            @RequestBody StudioRequest request
    ) {
        // TODO 토큰을 통해 파싱한 업체 객체 데이터 가지고 오기

        StudioResponse response = studioBusiness.updateProduct(1, productId, request);
        log.debug("Update StudioResponse : {}", response);

        return API.OK(response);
    }


    @DeleteMapping("/delete/{productId}")
    @Operation(summary = "스튜디오 상품 삭제", description = "스튜디오 엔터티 삭제")
    public API<String> deleteStudio(
            @PathVariable long productId
    ) {
        // TODO 토큰을 통해 파싱한 업체 객체 데이터 가지고 오기

        studioBusiness.deleteProduct(2, productId);
        return API.OK("상품이 삭제되었습니다.");
    }


    @GetMapping("/list/{companyId}")
    @Operation(summary = "스튜디오 상품 리스트", description = "삭제된 상품 제외한 모든 상품 보여주기")
    public API<List<StudioResponse>> getStudioList(
            @PathVariable long companyId
    ) {
        // TODO 요청한 업체의 리스트인지 확인해야함
        List<StudioResponse> studioResponseList = studioBusiness.getValidProductList(companyId);
        log.debug("DressList StudioResponseList : {}", studioResponseList);

        return API.OK(studioResponseList);
    }

}
