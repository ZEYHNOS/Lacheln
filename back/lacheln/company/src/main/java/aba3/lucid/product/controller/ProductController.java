package aba3.lucid.product.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.dto.DressResponse;
import aba3.lucid.product.business.DressBusiness;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final DressBusiness dressBusiness;


    @PostMapping("/dress/{companyId}")
    public API<DressResponse> registerDress(
            @PathVariable long companyId,
            @Valid DressRequest req
        ) {
        DressResponse res = dressBusiness.registerProduct(companyId, req);

        return API.OK();
    }


}
