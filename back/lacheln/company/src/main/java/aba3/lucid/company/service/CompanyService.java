package aba3.lucid.company.service;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.dto.CompanyRequest;
import aba3.lucid.domain.company.dto.CompanyResponse;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

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


    //삭제 과정에서 문제가 발생하면 롤백할 수 있도록 하기 위함- transactional 쓰는 이유
    @Transactional
    public void deleteCompany(Long cpId) {
        CompanyEntity company = companyRepository.findById(cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        companyRepository.delete(company);
    }
    public CompanyEntity findByIdWithThrow(Long cpId) {
        return companyRepository.findById(cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }

    public void updateCompany(CompanyRequest companyRequest) {


    }


}
