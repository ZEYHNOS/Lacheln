package aba3.lucid.product.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.packages.dto.ProductPackageInsertResponse;
import aba3.lucid.domain.product.dto.option.ProductResponse;
import aba3.lucid.product.business.ProductBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/send/message")
    public void sendMessage(
            @RequestParam String message
    ) {
        productBusiness.sendMessage(message);
    }


    @PostMapping("/package/register/{packageId}")
    @Operation(summary = "패키지에 상품 등록", description = "패키지에 등록된 업체가 상품을 1개 등록하기")
    public API<ProductPackageInsertResponse> packageRegister(
            @PathVariable Long packageId,
            @RequestParam Long productId
    ) {
        // TODO 업체 정보 가지고 오기
        ProductPackageInsertResponse response = productBusiness.packageRegister(packageId, 1L, productId);

        return API.OK(response);
    }

    @GetMapping("/list/{companyId}")
    @Operation(summary = "해당 번호의 업체 상품 리스트 반환", description = "상품 리스트 반환 (삭제된 상품 제외)")
    // TODO 요청한 업체의 리스트인지 확인해야함
    public API<List<ProductResponse>> getProductList(
            @PathVariable Long companyId
    ) {
        List<ProductResponse> productResponseList = productBusiness.getValidProductList(companyId);

        return API.OK(productResponseList);
    }
}
