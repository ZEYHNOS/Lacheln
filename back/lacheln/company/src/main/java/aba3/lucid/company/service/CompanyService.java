package aba3.lucid.company.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.convertor.CompanyConvertor;
import aba3.lucid.domain.company.dto.CompanyRequest;
import aba3.lucid.domain.company.dto.CompanyUpdateRequest;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyConvertor companyConvertor;

    public CompanyEntity findByIdAndMatchCategoryWithThrow(long companyId, CompanyCategory companyCategory) {
        CompanyEntity companyEntity = findByIdWithThrow(companyId);

        // 카테고리가 같지 않을 때
        if (!companyEntity.getCpCategory().equals(companyCategory)) {
            log.info("company, {}", companyEntity.getCpId());
            log.info("category, {}", companyCategory);
            log.info("dbcategory, {}", companyEntity.getCpCategory());
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }

        return companyEntity;
    }

    public CompanyEntity findByIdWithThrow(long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }



    public CompanyEntity findByIdWithThrow(Long cpId) {
        return companyRepository.findById(cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }

    public CompanyEntity findByCpEmailWithThrow(String cpEmail) {
        return companyRepository.findByCpEmail(cpEmail)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }


    //메소드에 전달된 existingCompany와 request가 null인지 확인하고
    //만약 둘 중 하나라도 null이면, ApiException을 발생시켜 잘못된 파라미터임을 알립니다. 이는 안정성을 위해 필수이다
//    public CompanyEntity updateCompany(CompanyEntity existingCompany, CompanyUpdateRequest request, BCryptPasswordEncoder passwordEncoder) {
//        existingCompany.updateCompanyRequest(request,passwordEncoder);
//        return companyRepository.save(existingCompany);
//
//    }

    public void deleteCompany(CompanyEntity company) {
        companyRepository.delete(company);
    }





}
