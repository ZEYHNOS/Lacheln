package aba3.lucid.packages.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.packages.dto.PackageProductRequest;
import aba3.lucid.domain.packages.dto.PackageRequest;
import aba3.lucid.domain.packages.dto.PackageResponse;
import aba3.lucid.packages.business.PackageBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/product/package")
@RequiredArgsConstructor
@Tag(name = "Package Controller", description = "패키지 관련 API")
public class PackageController {

    private final PackageBusiness packageBusiness;

    // 패키지 생성하기

    /**
     * 1. 업체(방장)이 패키지 생성하기 (완료)
     *    - 패키지 명, 패키지 설명 데이터만 받고 생성하기
     * 2. 업체(방장)이 다른 업체 2개를 추가하기
     *    - 모두 다른 업체인지(카테고리 포함) 확인하기
     * 3. 모든 업체가 하나의 상품을 등록하기
     *    - 이미 상품을 등록해놓은 상태여야한다.
     * 4. 업체가 상품을 등록하면 승인 여부를 선택할 수 있게 된다
     *    - 업체(방장)는 방장 제외 다른 업체가 모두 승인하기 전까지 승인을 누를 수 없다
     *    - 다른 업체들이 모두 승인 버튼을 누르고 할인율, 개시일, 종료일, 대표이미지가 모두 등록되어있어야 승인을 누를 수 있다
     * 5. 방장이 승인 버튼을 누르면 그 순간부터 해당 상품은 수정, 삭제를 할 수 없다.
     */
    @PostMapping("/register/{companyId}")
    @Operation(summary = "새로운 패키지 생성")
    public API<PackageResponse> packageRegister(
            @RequestBody PackageRequest packageRequest,
            @PathVariable long companyId
    ) {
       PackageResponse packageResponse = packageBusiness.packageRegister(packageRequest, companyId);
       log.info("Package Register Response : {}", packageResponse);
       return API.OK(packageResponse);
    }

    // 패키지 리스트 보기


    // 패키지 수정하기


    // 패키지 삭제하기


    // 업체(방장)가 다른 업체 초대하기
    @PostMapping("/invitation/{packageId}")
    @Operation(summary = "방장이 다른 업체 초대하기", description = "한 번에 하나의 업체를 초대")
    public API<String> invitationCompanies(
            @RequestParam long companyId,
            @PathVariable long packageId
    ) {
        packageBusiness.invitationPackageGroup(packageId, companyId);

        return API.OK("초대가 완료되었습니다.");
    }


    // 패키지 상품 등록하기
    @PostMapping("/register")
    @Operation(summary = "한 업체가 패키지에 상품을 등록", description = "패키지에 소속된 업체가 해당 패키지에 상품 등록")
    public API<String> productRegister(
            @RequestBody PackageProductRequest request
    ) {
        packageBusiness.productRegister(request, 1);

        return API.OK("패키지에 상품을 등록했습니다.");
    }



}
