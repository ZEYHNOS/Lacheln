package aba3.lucid.packages.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.packages.dto.*;
import aba3.lucid.packages.business.PackageBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/package")
@RequiredArgsConstructor
@Tag(name = "Package Controller", description = "패키지 관련 API")
public class PackageController {

    private final PackageBusiness packageBusiness;

    // 패키지 등록
    @PostMapping("/register")
    @Operation(summary = "패키지 등록", description = "패키지 등록 하기")
    public API<PackageResponse> packageRegister(
            @Valid
            @RequestBody
            PackageRegisterRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
//        PackageResponse response = packageBusiness.packageRegister(request, user.getCompanyId());
        log.info("Package Register Request : {}", request);
        PackageResponse response = packageBusiness.packageRegister(request, 1L);

        return API.OK(response);
    }


    // 패키지 정보 수정하기
    @PutMapping("/update/{packageId}")
    @Operation(summary = "패키지 업데이트", description = "패키지 업데이트 하기")
    public API<PackageResponse> packageUpdate(
            @Valid
            @RequestBody
            PackageUpdateRequest request,
            @PathVariable Long packageId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
//        PackageResponse response = packageBusiness.packageUpdate(request, user.getCompanyId(), packageId);
        PackageResponse response = packageBusiness.packageUpdate(request, 1L, packageId);

        return API.OK("");
    }


    // 패키지 업로드하기
    @PostMapping("/upload/{packageId}")
    @Operation(summary = "패키지 업로드하기", description = "모든 상품이 등록되었을 때 private -> public으로 변경")
    public API<PackageResponse> packageUpload(
            @PathVariable Long packageId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
//        PackageResponse response = packageBusiness.packageUpload(packageId, user.getCompanyId());
        PackageResponse response = packageBusiness.packageUpload(packageId, 1L);

        return API.OK(response);
    }

    @GetMapping("/list")
    @Operation(summary = "업체가 속한 패키지 리스트 반환")
    public API<List<PackageResponse>> getPackageList(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
//        List<PackageResponse> packageResponseList = packageBusiness.getPackageList(user.getCompanyId());
        List<PackageResponse> packageResponseList = packageBusiness.getPackageList(1L);

        return API.OK(packageResponseList);
    }

    @GetMapping("/{packageId}")
    @Operation(summary = "패키지 상세 정보")
    public API<PackageResponse> getPackageInfo(
            @PathVariable Long packageId
    ) {
        PackageResponse response = packageBusiness.getPackageByPackageId(packageId);

        return API.OK(response);
    }

    @DeleteMapping("/{packageId}")
    @Operation(summary = "패키지에서 상품 삭제하기")
    public API<String> deletePackageProduct(
            @PathVariable Long packageId,
            @RequestParam Long productId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
//        packageBusiness.deletePackageProduct(user.getCompanyId(), packageId, productId);
        packageBusiness.deletePackageProduct(1L, packageId, productId);

        return API.OK("상품이 삭제되었습니다.");
    }


    @GetMapping
    @Operation(summary = "패키지 리스트", description = "유저용")
    public API<List<PackageUserViewListResponse>> getPackageList() {
        List<PackageUserViewListResponse> packageResponseList = packageBusiness.getPackageList();

        return API.OK(packageResponseList);
    }


    @GetMapping("/info/{packageId}")
    @Operation(summary = "패키지 상세 정보", description = "유저용")
    public API<PackageDetailInfoUserViewResponse> getPackageInfoUserView(
            @PathVariable Long packageId
    ) {
        PackageDetailInfoUserViewResponse result = packageBusiness.getPackageDetailInfo(packageId);

        return API.OK(result);
    }


}
