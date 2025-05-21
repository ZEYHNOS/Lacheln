package aba3.lucid.product.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.api.Pagination;
import aba3.lucid.common.api.PaginationConverter;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.packages.dto.ProductPackageInsertResponse;
import aba3.lucid.domain.product.dto.ProductStatusUpdateRequest;
import aba3.lucid.domain.product.dto.option.ProductResponse;
import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.product.business.ProductBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "Product API 관련")
public class ProductController {

    private final ProductBusiness productBusiness;
    private final PaginationConverter paginationConverter;

    @GetMapping()
    public API<List<ProductResponse>> getProductList(
            @RequestParam(defaultValue = "D") CompanyCategory category,
            @RequestParam(defaultValue = "0") Integer minimum,
            @RequestParam(defaultValue = "1000000") Integer maximum,
            @RequestParam(defaultValue = "true") Boolean isDesc,
            @PageableDefault(page = 0, size = 12) Pageable pageable
        ) {
        log.info("정렬 중 :  카테고리 {}, minimum : {}, maximum : {}, isDesc : {}, page : {}", category, minimum, maximum, isDesc, pageable);
        Page<ProductResponse> productResponsePage = productBusiness.getProductList(category, minimum, maximum, isDesc, pageable);
        Pagination pagination = paginationConverter.createPagination(productResponsePage, pageable, "desc");
        log.info("product Response : {}", productResponsePage);

        return API.OK(productResponsePage.stream().toList(), pagination);
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
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false) ProductStatus status
    ) {
//        List<ProductResponse> productResponseList = productBusiness.getValidProductList(customUserDetails.getCompanyId());
        List<ProductResponse> productResponseList = productBusiness.getValidProductList(1L, status);

        return API.OK(productResponseList);
    }

    @PostMapping("/image/upload")
    @Operation(summary = "상품 이미지 업로드")
    public API<List<String>> imagesUpload(
            @RequestPart List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws IOException {
//        List<String> imageUrlList = productBusiness.productImagesUpload(user.getCompanyId(), images);
        List<String> imageUrlList = productBusiness.productImagesUpload(1L, images);

        return API.OK(imageUrlList);
    }

    @PostMapping("/status")
    @Operation(summary = "상품 상태 변경")
    public API<String> updateStatus(
            @RequestBody ProductStatusUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        productBusiness.updateStatus(user.getCompanyId(), request);

        return API.OK("상품 상태가 변경되었습니다.");
    }

    @PostMapping("/upload/{productId}")
    @Operation(summary = "상품 업로드 하기")
    public API<ProductResponse> uploadProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
//        productBusiness.uploadProduct(productId, user.getCompanyId);
        ProductResponse response = productBusiness.uploadProduct(productId, 1L);

        return API.OK(response);
    }
}
