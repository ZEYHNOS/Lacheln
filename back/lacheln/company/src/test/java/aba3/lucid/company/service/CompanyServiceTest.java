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
                .email("Ceri88@gmail.com")
                .password("84u4848hf")
                .passwordConfirm("84u4848hf")
                .name("Shahid")
//                .repName("Ganbarov")
//                .mainContact("00105447795")
                .address("Daegu City")
                .bnRegNo("123-45-67890")
                .mos("2019-서울강남-01234")
//                .profile("default_profile.jpg")
//                .explain("A company for testing purposes.")
//                .category(CompanyCategory.D)
//                .status(CompanyStatus.ACTIVATE)
                .contact("01012345678")
//                .fax("02-1234-5678")
                .postalCode("12345")
                .build();
        Long savedId = companyBusiness.registerCompany(companyRequest).getId();
        CompanyEntity savedCompany = companyRepository.findById(savedId).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND,"업체가 없습니다"));
        assertEquals(companyRequest.getName(), savedCompany.getCpName());
        assertEquals(companyRequest.getEmail(), savedCompany.getCpEmail());
        assertEquals(companyRequest.getBnRegNo(), savedCompany.getCpBnRegNo());
        assertEquals(companyRequest.getMos(), savedCompany.getCpMos());
        assertEquals(companyRequest.getContact(), savedCompany.getCpContact());
//        assertEquals(companyRequest.getFax(), savedCompany.getCpFax());
    }

}
