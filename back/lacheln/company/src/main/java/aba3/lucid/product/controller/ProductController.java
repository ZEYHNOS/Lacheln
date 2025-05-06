package aba3.lucid.product.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.packages.dto.ProductPackageInsertResponse;
import aba3.lucid.domain.product.dto.option.ProductResponse;
import aba3.lucid.product.business.ProductBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping("/package/register/{packageId}")
    @Operation(summary = "패키지에 상품 등록", description = "패키지에 등록된 업체가 상품을 1개 등록하기")
    public API<ProductPackageInsertResponse> packageRegister(
            @PathVariable Long packageId,
            @RequestParam Long productId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
//        ProductPackageInsertResponse response = productBusiness.packageRegister(packageId, customUserDetails.getCompanyId(), productId);
        ProductPackageInsertResponse response = productBusiness.packageRegister(packageId, 1L, productId);

        return API.OK(response);
    }

    @GetMapping("/list")
    @Operation(summary = "해당 번호의 업체 상품 리스트 반환", description = "상품 리스트 반환 (삭제된 상품 제외)")
    public API<List<ProductResponse>> getProductList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
//        List<ProductResponse> productResponseList = productBusiness.getValidProductList(customUserDetails.getCompanyId());
        List<ProductResponse> productResponseList = productBusiness.getValidProductList(1L);

        return API.OK(productResponseList);
    }

    @PostMapping("/image/upload")
    @Operation(summary = "상품 이미지 업로드")
    public API<List<String>> imagesUpload(
            @RequestPart List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws IOException {
//        List<String> imageUrlList = productBusiness.productImagesUpload(customUserDetails.getCompanyId(), images);
        List<String> imageUrlList = productBusiness.productImagesUpload(1L, images);

        return API.OK(imageUrlList);
    }
}
