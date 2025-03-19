package aba3.lucid.company.service;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.convertor.CompanyConvertor;
import aba3.lucid.domain.company.dto.CompanyRequest;
import aba3.lucid.domain.company.dto.CompanyResponse;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    //메소드에 전달된 existingCompany와 request가 null인지 확인하고
    //만약 둘 중 하나라도 null이면, ApiException을 발생시켜 잘못된 파라미터임을 알립니다. 이는 안정성을 위해 필수이다
    public CompanyEntity updateCompany( CompanyEntity existingCompany,CompanyRequest request) {
        if(existingCompany == null || request == null) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER, "업데이트를 위한 잘못된 매개변수");
        }
        existingCompany.updateCompanyRequest(request);
        return companyRepository.save(existingCompany);

    }

    public void deleteCompany(CompanyEntity company) {
        companyRepository.delete(company);
    }




}
