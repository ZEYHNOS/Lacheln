package aba3.lucid.company.business;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.CompanyCode;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.convertor.CompanyConvertor;
import aba3.lucid.domain.company.dto.CompanyRequest;
import aba3.lucid.domain.company.dto.CompanyResponse;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CompanyBusiness {
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final CompanyConvertor companyConvertor;

    //ApplicationContext를 생성자의 의존성 주입을 통해 받아오고 있습니다:
    private final ApplicationContext applicationContext;

    public CompanyBusiness(CompanyService companyService,
                           CompanyRepository companyRepository,
                           CompanyConvertor companyConvertor,
                           ApplicationContext applicationContext) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
        this.companyConvertor = companyConvertor;
        this.applicationContext = applicationContext;
    }

    //encodePassword() 메서드가 호출될 때 applicationContext.getBean(PasswordEncoder.class)를
    // 사용하여 Spring 컨테이너에서 PasswordEncoder 빈을 찾아 반환합니다
    public String encodePassword(String rawPassword, String email) {
        aba3.lucid.common.password.PasswordEncoder passwordEncoder =
                applicationContext.getBean(aba3.lucid.common.password.PasswordEncoder.class);
        return passwordEncoder.encrypt(email, rawPassword);

    }
    //passwordEncoder.encode(rawPassword)를 호출하여 비밀번호를 암호화합니다.

    public CompanyResponse registerCompany(CompanyRequest request) {
        if(request == null) {
            throw  new ApiException(ErrorCode.BAD_REQUEST, "CompanyRequest 값을 받지 못했습니다");
        }

        if(!request.getCpPassword().equals(request.getCpPasswordConfirm())) {
            throw  new ApiException(ErrorCode.BAD_REQUEST, "비밀번호와 비밀번호 확인 값이 일치하지 않습니다");
        }

        validateDuplicateCompany(request.getCpEmail());
        //encodePassword 메서드로  email을 인자로 받도록 했습니다
        String hashedPassword = encodePassword(request.getCpPassword(), request.getCpEmail());
        CompanyEntity companyEntity = companyConvertor.toEntity(request, hashedPassword);

        CompanyEntity savedCompanyEntity = companyConvertor.toEntity(request, hashedPassword);
        CompanyEntity savedCompanyEntitySaved = companyRepository.save(savedCompanyEntity);
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



    //삭제 과정에서 문제가 발생하면 롤백할 수 있도록 하기 위함- transactional 쓰는 이유
    @Transactional
    public void deleteCompany(Long cpId) {
        CompanyEntity company = companyRepository.findById(cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        companyService.deleteCompany(company);
    }


    public CompanyResponse updateCompany(CompanyRequest companyRequest, Long companyId) {
        if(companyRequest == null || Validator.isInvalidId(companyId)) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER, "CompanyRequest 값을 받지 못했습니다");
        }
        //companyService.findByIdWithThrow(companyId)를 호출하여, 주어진 companyId에 해당하는 회사를 조회합니다.
        CompanyEntity existingCompany = companyService.findByIdWithThrow(companyId);
        if(existingCompany == null) {
            throw new ApiException(ErrorCode.NOT_FOUND, "업체를 찾을 수 없습니다");
        }

        CompanyEntity updateCompany = companyService.updateCompany(existingCompany, companyRequest);

        return companyConvertor.toResponse(updateCompany);

    }




}
