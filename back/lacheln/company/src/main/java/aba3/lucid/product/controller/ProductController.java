package aba3.lucid.product.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.dto.DressResponse;
import aba3.lucid.product.business.DressBusiness;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final DressBusiness dressBusiness;


    @PostMapping("/dress/{companyId}")
    public API<DressResponse> registerDress(
            @PathVariable long companyId,
            @Valid
            @RequestBody DressRequest req
        ) {
        DressResponse res = dressBusiness.registerProduct(companyId, req);

        return API.OK(res);
    }


}
