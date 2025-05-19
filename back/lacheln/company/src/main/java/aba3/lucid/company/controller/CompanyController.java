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
            @PathVariable Long companyId,
            @RequestBody CompanyProfileSetRequest request

    ) {
          CompanyProfileSetResponse response = companyBusiness.updateCompanyProfile(companyId,request);
          log.debug("Update CompanyProfileSetResponse: {}", response);
          return API.OK(response);

    }

    @PutMapping("/update/{companyId}")
    public API<CompanyUpdateResponse> updateCompany (
            @RequestBody CompanyUpdateRequest companyUpdateRequest

    ){
        return companyBusiness.updateCompany(companyUpdateRequest, AuthUtil.getCompanyId());
    }

    @GetMapping("/search/{email}")
    public API<CompanyResponse> searchCompany (
            @PathVariable String email
    ) {
        CompanyResponse companyResponse = companyBusiness.searchCompany(email);
        return API.OK(companyResponse);
    }

    @DeleteMapping("/delete/{companyId}")
    public API<CompanyResponse> deleteCompany (

            @PathVariable long companyId,
            @Valid
            @RequestBody CompanyRequest companyRequest
    ){
        companyBusiness.deleteCompany(companyId);
        return API.OK("업체가 삭제되었습니다");
    }

    @GetMapping("/category")
    @Operation(summary = "해당 업체의 카테고리")
    public CompanyCategory getCategory() {
        return companyBusiness.getCategory(1L);
    }


}
