package aba3.lucid.company.controller;


import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.company.business.CompanyBusiness;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.dto.*;
import aba3.lucid.domain.company.enums.CompanyCategory;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyBusiness companyBusiness;
    private final CompanyService companyService;


    @PostMapping("/signup")
    public API<CompanyResponse> registerCompany (
            @RequestBody CompanyRequest companyRequest

    ) {
            CompanyResponse companyResponse = companyBusiness.registerCompany( companyRequest);
            log.debug("Register CompanyResponse: {}", companyResponse);
            return API.OK(companyResponse);

    }

    @PostMapping("/{companyId}/profile")
    public API<CompanyProfileSetResponse> setCompanyProfile(
            @AuthenticationPrincipal CustomUserDetails company,
            @RequestBody CompanyProfileSetRequest request,
            @RequestPart MultipartFile profileImgFile


    ) throws IOException {
          CompanyProfileSetResponse response = companyBusiness.setCompanyProfile(company.getCompanyId(),request,profileImgFile);
          log.debug("Update CompanyProfileSetResponse: {}", response);
          return API.OK(response);

    }

    @PutMapping("/update/{companyId}")
    public API<CompanyUpdateResponse> updateCompany (
            @AuthenticationPrincipal CustomUserDetails company,
            @RequestPart MultipartFile updatedProfileImgFile,
            @RequestBody CompanyUpdateRequest companyUpdateRequest

    ) throws IOException {
        CompanyUpdateResponse updateResponse = companyBusiness.updateCompany(company.getCompanyId(), updatedProfileImgFile,companyUpdateRequest);
        return API.OK(updateResponse);
    }

    @GetMapping("/search/{email}")
    public API<CompanyResponse> searchCompany (
            @PathVariable String email
    ) {
        CompanyResponse companyResponse = companyBusiness.searchCompany(email);
        return API.OK(companyResponse);
    }


    @GetMapping("/category")
    @Operation(summary = "해당 업체의 카테고리")
    public CompanyCategory getCategory(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return companyBusiness.getCategory(customUserDetails.getCompanyId());
    }


    @GetMapping("/info/{id}")
    public API<CompanyResponse> getCompanyInfo(
            @PathVariable Long id
    ) {
        CompanyResponse response = companyBusiness.getCompanyInfo(id);
        return API.OK(response);
    }

    @GetMapping("/me")
    public Long getCompanyId(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return user.getCompanyId();
    }

    @GetMapping("/all/{pages}")
    @Operation(summary = "회원 정버를 가져오기")
    public API<CompanyAllResponse<CompanyResponse>> getAllCompanyMembers (
            @PathVariable Integer pages

            ) {
        return companyBusiness.getAllCompanies(pages);
    }

//    @GetMapping("/all/{page}")
//    @Operation(summary = "회원 정버를 가져오기")
//    public API<CompanyAllResponse<CompanyResponse>> getAllCompanyMembers (
//            @PathVariable Integer page
//    ) {
//        CompanyAllResponse<CompanyResponse> responses = companyBusiness.getAllCompanies(page);
//        log.debug("CompanyAllResponse(실제 타입): {}", responses.getClass());
//        log.debug("CompanyAllResponse(내용): {}", responses);
//        API<CompanyAllResponse<CompanyResponse>> apiResult = API.OK(responses);
//        log.debug("API 결과(내용): {}", apiResult);
//        return apiResult;
//    }

    @PutMapping("/password/change")
    public API<CompanyPasswordUpdateResponse> newPassword (
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CompanyPasswordUpdateRequest request
    ) {
        CompanyPasswordUpdateResponse companyPasswordUpdateResponse =companyBusiness.updatePassword(request,customUserDetails.getCompanyId());
        return API.OK(companyPasswordUpdateResponse);
    }

    @GetMapping("/list")
    public API<CompanyUpdateResponse> getCompanyList (
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
       CompanyUpdateResponse response = companyBusiness.getUrlList(customUserDetails.getCompanyId());
       return API.OK(response);
    }

    @GetMapping("/today_new_members")
    public API<DashboardResponse> getTodayNewMembers() {
        long newCompanies = companyBusiness.getTodayNewCompanyCount().getData();

        DashboardResponse response = DashboardResponse.builder()
                .newCompanies(newCompanies)
                .totalNewMembers(newCompanies)
                .build();

        return API.OK(response);
    }

    @GetMapping("/month_members")
    public API<List<Object[]>> getMonthMembers () {
        return companyBusiness.getMonthlyJoinStats();
    }

//    @GetMapping("/today-reports")
//    public API<Long> getTodayReports() {
//        return reportBusiness.getTodayReportCount(); // Report 구현 필요
//    }


}
