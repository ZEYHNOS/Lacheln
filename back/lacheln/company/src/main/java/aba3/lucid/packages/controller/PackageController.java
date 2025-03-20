package aba3.lucid.packages.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.packages.dto.PackageGroupCreateRequest;
import aba3.lucid.domain.packages.dto.PackageRequest;
import aba3.lucid.domain.packages.dto.PackageResponse;
import aba3.lucid.packages.business.PackageBusiness;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/product/package")
@RequiredArgsConstructor
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
    @PostMapping("/invitation")
    public API<String> invitationCompanies(
            @RequestBody PackageGroupCreateRequest request
    ) {
        // TODO 요청 업체 ID 가지고 오기
        packageBusiness.createPackageGroup(request, 1);

        return API.OK("초대가 완료되었습니다.");
    }


}
