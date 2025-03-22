package aba3.lucid.product.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.dto.option.ProductResponse;
import aba3.lucid.product.business.ProductBusiness;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "Product API 관련")
public class ProductController {

    private final ProductBusiness productBusiness;

    @GetMapping("/")
    public API<List<ProductResponse>> getProductList(
            @RequestParam(required = false) CompanyCategory category,
            @RequestParam(required = false) int minimum,
            @RequestParam(required = false) int maximum,
            @RequestParam(defaultValue = "true") boolean isDesc
    ) {
        List<ProductResponse> productResponseList = productBusiness.getProductList(category, minimum, maximum, isDesc);

        return API.OK(productResponseList);
    }
}
