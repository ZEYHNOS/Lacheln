package aba3.lucid.company.controller;


import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.company.business.CompanyBusiness;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.dto.*;
import aba3.lucid.domain.company.enums.CompanyCategory;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyBusiness companyBusiness;


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
            @RequestBody CompanyProfileSetRequest request

    ) {
          CompanyProfileSetResponse response = companyBusiness.updateCompanyProfile(company.getCompanyId(),request);
          log.debug("Update CompanyProfileSetResponse: {}", response);
          return API.OK(response);

    }

    @PutMapping("/update/{companyId}")
    public API<CompanyUpdateResponse> updateCompany (
            @AuthenticationPrincipal CustomUserDetails company,
            @RequestBody CompanyUpdateRequest companyUpdateRequest

    ){
        return companyBusiness.updateCompany(companyUpdateRequest, company.getCompanyId());
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


}
