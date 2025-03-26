package aba3.lucid.company.business;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.password.CustomPasswordEncoder;
import aba3.lucid.common.status_code.CompanyCode;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.convertor.CompanyConvertor;
import aba3.lucid.domain.company.convertor.CompanySetConvertor;
import aba3.lucid.domain.company.dto.*;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.enums.CompanyStatus;
import aba3.lucid.domain.company.repository.CompanyRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class CompanyBusiness {
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final CompanyConvertor companyConvertor;
    private final CustomPasswordEncoder customPasswordEncoder;

    //ApplicationContext를 생성자의 의존성 주입을 통해 받아오고 있습니다:
    private final ApplicationContext applicationContext;
    private final CompanySetConvertor companySetConvertor;

    public CompanyBusiness(CompanyService companyService,
                           CompanyRepository companyRepository,
                           CompanyConvertor companyConvertor,
                           ApplicationContext applicationContext,
                           CustomPasswordEncoder customPasswordEncoder, CompanySetConvertor companySetConvertor)
    {

        this.companyService = companyService;
        this.companyRepository = companyRepository;
        this.companyConvertor = companyConvertor;
        this.applicationContext = applicationContext;
        this.customPasswordEncoder = customPasswordEncoder;
        this.companySetConvertor = companySetConvertor;
    }

    //encodePassword() 메서드가 호출될 때 applicationContext.getBean(PasswordEncoder.class)를
    // 사용하여 Spring 컨테이너에서 PasswordEncoder 빈을 찾아 반환합니다
    public String encodePassword(String rawPassword, String email) {
        return customPasswordEncoder.encrypt(email, rawPassword);

    }
    //passwordEncoder.encode(rawPassword)를 호출하여 비밀번호를 암호화합니다.

    public CompanyResponse registerCompany(CompanyRequest request) {
        if(request == null) {
            throw  new ApiException(ErrorCode.BAD_REQUEST, "CompanyRequest 값을 받지 못했습니다");
        }

        if(!request.getPassword().equals(request.getPasswordConfirm())) {
            throw  new ApiException(ErrorCode.BAD_REQUEST, "비밀번호와 비밀번호 확인 값이 일치하지 않습니다");
        }

        validateDuplicateCompany(request.getEmail());
        //encodePassword 메서드로  email을 인자로 받도록 했습니다
        String hashedPassword = encodePassword(request.getPassword(), request.getEmail());

//        CompanyEntity savedCompanyEntity = companyConvertor.toEntity(request, hashedPassword);
//        CompanyEntity savedCompanyEntitySaved = companyRepository.save(savedCompanyEntity);
        CompanyEntity companyEntity = companyConvertor.toEntity(request, hashedPassword);

        companyEntity.setCpRepName("임시대표");
        companyEntity.setCpMainContact("01000000000");
        companyEntity.setCpStatus(CompanyStatus.SUSPENSION);
        companyEntity.setCpProfile("default.png");
        companyEntity.setCpCategory(CompanyCategory.S);


        CompanyEntity savedEntity = companyRepository.save(companyEntity);
        return companyConvertor.toResponse(savedEntity);
    }
    public CompanyProfileSetResponse updateCompanyProfile(Long companyId, CompanyProfileSetRequest request) {
        CompanyEntity entity = companyRepository.findById(companyId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "회사 정보를 찾을 수 없습니다."));

        entity.setCpRepName(request.getRepName());
        entity.setCpMainContact(request.getMainContact());
        entity.setCpStatus(request.getStatus());
        entity.setCpProfile(request.getProfile());
        entity.setCpExplain(request.getExplain());
        entity.setCpCategory(request.getCategory());
        entity.setCpFax(request.getFax());

        CompanyEntity updated = companyRepository.save(entity);
        return companySetConvertor.toResponse(updated);
    }

    private void validateDuplicateCompany(String email) {
        if(companyRepository.existsByCpEmail(email)) {
            throw new ApiException(CompanyCode.ALREADY_REGISTERED_COMPANY, "이미 등롣된 이메일입니다.");
        }
    }


    public CompanyEntity findByIdWithThrow(long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(()-> new ApiException(ErrorCode.NOT_FOUND, "회사를 찾을 수 없습니다"));
    }



    //삭제 과정에서 문제가 발생하면 롤백할 수 있도록 하기 위함- transactional 쓰는 이유
    @Transactional
    public void deleteCompany(long cpId) {
        CompanyEntity company = companyRepository.findById(cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        companyService.deleteCompany(company);
    }


    public CompanyResponse updateCompany(CompanyRequest companyRequest, long companyId) {
        Validator.throwIfNull(companyRequest);
        Validator.throwIfInvalidId(companyId);
        //companyService.findByIdWithThrow(companyId)를 호출하여, 주어진 companyId에 해당하는 회사를 조회합니다.
        CompanyEntity existingCompany = companyService.findByIdWithThrow(companyId);
        if(existingCompany == null) {
            throw new ApiException(ErrorCode.NOT_FOUND, "업체를 찾을 수 없습니다");
        }

        CompanyEntity updateCompany = companyService.updateCompany(existingCompany, companyRequest);

        return companyConvertor.toResponse(updateCompany);

    }

    public CompanyResponse searchCompany(CompanyRequest companyRequest,String email) {
        Optional<CompanyEntity> companyOpt = companyRepository.findByCpEmail(email);
        if(companyOpt.isPresent()) {
            CompanyEntity company = companyOpt.get();
            return companyConvertor.toResponse(company);
        }else {
            throw new ApiException(ErrorCode.NOT_FOUND, "회사를 찾을 수 앖습니다");
        }
    }


//    public List<CompanyRequest> searchCompany(CompanyRequest companyRequest, String email) {
//        List<CompanyEntity> companyEntityList = companyRepository.findAll();
//        List<CompanyRequest> companyRequestList = new ArrayList<>();
//        for(CompanyEntity companyEntity : companyEntityList) {
//            companyRequestList.add(
//        }
//    }

    public CompanyLoginResponse login (CompanyLoginRequest request) {
        if(request == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "LoginRequest 값을 받지 못했습니다");
        }
        CompanyEntity companyEntity = companyService.findByCpEmailWithThrow(request.getCpEmail());

        String hashedPassword = encodePassword(request.getCpPassword(), request.getCpEmail());
        if(!companyEntity.getCpPassword().equals(hashedPassword)) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER, "비밀번호가 틀렸습니다");
        }

        return CompanyLoginResponse.builder()
                .cpId(companyEntity.getCpId())
                .cpEmail(companyEntity.getCpEmail())
                .cpName(companyEntity.getCpName())
                .build();
    }




}
