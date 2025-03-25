package aba3.lucid.company.controller;


import aba3.lucid.common.api.API;
import aba3.lucid.company.business.CompanyBusiness;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.dto.CompanyLoginRequest;
import aba3.lucid.domain.company.dto.CompanyLoginResponse;
import aba3.lucid.domain.company.dto.CompanyRequest;
import aba3.lucid.domain.company.dto.CompanyResponse;
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
    private final CompanyService companyService;




    @PostMapping("/login")
    public API<CompanyLoginResponse> login(
            @Valid
            @RequestBody CompanyLoginRequest companyLoginRequest
    ) {
        CompanyLoginResponse companyLoginResponse = companyBusiness.login(companyLoginRequest);
        log.debug("Login CompanyResponse: {}", companyLoginResponse);
        return API.OK(companyLoginResponse);
    }

    @PostMapping("/signup")
    public API<CompanyResponse> registerCompany (
            @Valid
            @RequestBody CompanyRequest companyRequest

    ) {
            CompanyResponse companyResponse = companyBusiness.registerCompany( companyRequest);
            log.debug("Register CompanyResponse: {}", companyResponse);
            return API.OK(companyResponse);

    }

    @PutMapping("/update/{companyId}")
    public API<CompanyResponse> updateCompany (

            @PathVariable long companyId,
            @Valid
            @RequestBody CompanyRequest companyRequest

    ){
        CompanyResponse companyResponse = companyBusiness.updateCompany(companyRequest, companyId);
        return API.OK(companyResponse);
    }

//    @GetMapping("/search")
//    public API<CompanyResponse> searchCompany (
//            @PathVariable String cpEmail,
//            @Valid
//            @RequestBody CompanyRequest companyRequest
//    ) {
//        CompanyResponse companyResponse = companyBusiness.searchCompany(companyRequest,cpEmail);
//        return API.OK(companyResponse);
//    }

    @DeleteMapping("/delete/{companyId}")
    public API<CompanyResponse> deleteCompany (

            @PathVariable long companyId,
            @Valid
            @RequestBody CompanyRequest companyRequest
    ){
        companyBusiness.deleteCompany(companyId);
        return API.OK("업체가 삭제되었습니다");
    }




}
