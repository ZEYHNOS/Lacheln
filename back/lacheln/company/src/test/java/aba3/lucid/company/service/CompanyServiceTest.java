package aba3.lucid.company.service;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.company.business.CompanyBusiness;
import aba3.lucid.domain.company.dto.CompanyRequest;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.enums.CompanyStatus;
import aba3.lucid.domain.company.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {
    @Autowired
    CompanyService companyService;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    private CompanyBusiness companyBusiness;

    @Test
    @Rollback
    public void registerCompany() throws Exception {
        CompanyRequest companyRequest = CompanyRequest.builder()
                .cpEmail("Ceri@gmail.com")
                .cpPassword("84u4848hf")
                .cpPasswordConfirm("84u4848hf")
                .cpName("Shahid")
                .cpRepName("Ganbarov")
                .cpMainContact("00105447795")
                .cpAddress("Daegu City")
                .cpRole("COMPANY")
                .cpBnRegNo("879hdhdhhd")
                .cpMos("1234567890")
                .cpProfile("default_profile.jpg")
                .cpExplain("A company for testing purposes.")
                .cpCategory(CompanyCategory.D)
                .cpStatus(CompanyStatus.ACTIVATE)
                .cpContact("01012345678")
                .cpFax("02-1234-5678")
                .cpPostalCode("12345")
                .build();
        Long savedId = companyBusiness.registerCompany(companyRequest).getCpId();
        CompanyEntity savedCompany = companyRepository.findById(savedId).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND,"업체가 없습니다"));
        assertEquals(companyRequest.getCpName(), savedCompany.getCpName());
        assertEquals(companyRequest.getCpEmail(), savedCompany.getCpEmail());
        assertEquals(companyRequest.getCpRole(), savedCompany.getCpRole());
        assertEquals(companyRequest.getCpBnRegNo(), savedCompany.getCpBnRegNo());
        assertEquals(companyRequest.getCpMos(), savedCompany.getCpMos());
        assertEquals(companyRequest.getCpStatus(), savedCompany.getCpStatus());
        assertEquals(companyRequest.getCpProfile(), savedCompany.getCpProfile());
        assertEquals(companyRequest.getCpExplain(), savedCompany.getCpExplain());
        assertEquals(companyRequest.getCpCategory(), savedCompany.getCpCategory());
        assertEquals(companyRequest.getCpContact(), savedCompany.getCpContact());
        assertEquals(companyRequest.getCpFax(), savedCompany.getCpFax());
    }
}
