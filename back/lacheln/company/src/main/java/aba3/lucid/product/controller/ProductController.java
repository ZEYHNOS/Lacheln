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

@Slf4j
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "Dress Controller", description = "드레스 관련 API")
public class ProductController {

    private final DressBusiness dressBusiness;

    @PostMapping("/dress/{companyId}")
    @Operation(summary = "드레스 등록", description = "새로운 드레스 상품을 등록")
    public API<DressResponse> registerDress(
            @PathVariable long companyId,
            @Valid
            @RequestBody DressRequest req
        ) {
        DressResponse res = dressBusiness.registerProduct(companyId, req);
        log.info("DressResponse : {}", res);

        return API.OK(res);
    }


}
