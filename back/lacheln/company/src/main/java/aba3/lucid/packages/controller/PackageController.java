package aba3.lucid.packages.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.packages.dto.PackageRegisterRequest;
import aba3.lucid.domain.packages.dto.PackageResponse;
import aba3.lucid.domain.packages.dto.PackageUpdateRequest;
import aba3.lucid.packages.business.PackageBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
            PackageRegisterRequest request
    ) {
        // TODO 요청 업체 정보
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
            @PathVariable Long packageId
    ) {
        // TODO 요청 업체 정보
        PackageResponse response = packageBusiness.packageUpdate(request, 1L, packageId);

        return API.OK("");
    }


    // 패키지 업로드하기
    @PostMapping("/upload/{packageId}")
    @Operation(summary = "패키지 업로드하기", description = "모든 상품이 등록되었을 때 private -> public으로 변경")
    public API<PackageResponse> packageUpload(
            @PathVariable Long packageId
    ) {
        PackageResponse response = packageBusiness.packageUpload(packageId, 1L);

        return API.OK(response);
    }


}
