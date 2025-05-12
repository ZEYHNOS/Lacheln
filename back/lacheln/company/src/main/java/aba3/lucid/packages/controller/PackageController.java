package aba3.lucid.packages.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomAuthenticationToken;
import aba3.lucid.domain.packages.dto.PackageRegisterRequest;
import aba3.lucid.domain.packages.dto.PackageResponse;
import aba3.lucid.domain.packages.dto.PackageUpdateRequest;
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
            @AuthenticationPrincipal CustomAuthenticationToken customAuthenticationToken
    ) {
//        PackageResponse response = packageBusiness.packageRegister(request, customAuthenticationToken.getCompanyId());
        log.info("Package Register Request : {}", request);
        PackageResponse response = packageBusiness.packageRegister(request, 3L);

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
            @AuthenticationPrincipal CustomAuthenticationToken customAuthenticationToken
    ) {
//        PackageResponse response = packageBusiness.packageUpdate(request, customAuthenticationToken.getCompanyId(), packageId);
        PackageResponse response = packageBusiness.packageUpdate(request, 3L, packageId);

        return API.OK("");
    }


    // 패키지 업로드하기
    @PostMapping("/upload/{packageId}")
    @Operation(summary = "패키지 업로드하기", description = "모든 상품이 등록되었을 때 private -> public으로 변경")
    public API<PackageResponse> packageUpload(
            @PathVariable Long packageId,
            @AuthenticationPrincipal CustomAuthenticationToken customAuthenticationToken
    ) {
//        PackageResponse response = packageBusiness.packageUpload(packageId, customAuthenticationToken.getCompanyId());
        PackageResponse response = packageBusiness.packageUpload(packageId, 3L);

        return API.OK(response);
    }

    @GetMapping("/list")
    @Operation(summary = "업체가 속한 패키지 리스트 반환")
    public API<List<PackageResponse>> getPackageList(
//            @AuthenticationPrincipal CustomAuthenticationToken customAuthenticationToken
    ) {
//        List<PackageResponse> packageResponseList = packageBusiness.getPackageList(customAuthenticationToken.getCompanyId());
        List<PackageResponse> packageResponseList = packageBusiness.getPackageList(3L);

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
            @AuthenticationPrincipal CustomAuthenticationToken customAuthenticationToken
    ) {
//        packageBusiness.deletePackageProduct(customAuthenticationToken.getCompanyId(), packageId, productId);
        packageBusiness.deletePackageProduct(1L, packageId, productId);

        return API.OK("상품이 삭제되었습니다.");
    }


}
