package aba3.lucid.company.business;


import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.status_code.CompanyCode;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.convertor.CompanyConvertor;
import aba3.lucid.domain.company.dto.CompanyRequest;
import aba3.lucid.domain.company.dto.CompanyResponse;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Business
@RequiredArgsConstructor
public class CompanyBusiness {
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private CompanyConvertor companyConvertor;
    private BCryptPasswordEncoder passwordEncoder;

    public CompanyResponse registerCompany(CompanyRequest request) {
        if(request == null) {
            throw  new ApiException(ErrorCode.BAD_REQUEST, "CompanyRequest 값을 받지 못했습니다");
        }

        validateDuplicateCompany(request.getCpEmail());

        String hashedPassword = passwordEncoder.encode(request.getCpPassword());
        CompanyEntity companyEntity = companyConvertor.toEntity(request, hashedPassword);

        CompanyEntity savedCompanyEntity = companyRepository.save(companyEntity);
        log.info("CompanyEntity saved {}", savedCompanyEntity);
        return companyConvertor.toResponse(savedCompanyEntity);
    }

    private void validateDuplicateCompany(String email) {
        if(companyRepository.existsByCpEmail(email)) {
            throw new ApiException(CompanyCode.ALREADY_REGISTERED_COMPANY, "이미 등롣된 이메일입니다.");
        }
    }

    public CompanyEntity findByIdWithThrow(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(()-> new ApiException(ErrorCode.NOT_FOUND, "회사를 찾을 수 없습니다"));
    }


}
