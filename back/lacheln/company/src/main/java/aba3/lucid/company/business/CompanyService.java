package aba3.lucid.company.business;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.CompanyCode;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.company.CompanyBusinessException;

import aba3.lucid.domain.company.dto.CompanyRequest;
import aba3.lucid.domain.company.dto.CompanyResponse;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public CompanyResponse register(CompanyRequest request) {
        //중복 회원 검증
        validateDuplicateCompanyMem(request.getCpEmail());
        // 비밀번호 해싱
        String hashedPassword  = passwordEncoder.encode(request.getCpPassword());
        //DB에 저장
        CompanyEntity savedCompany = companyRepository.save(request.toEntity(hashedPassword));
        //응답 객체 변환 후 반환
        return CompanyResponse.fromEntity(savedCompany);
    }

    //중복 회원 검증 메서드
    private void validateDuplicateCompanyMem(String email) {
        if(companyRepository.existsByCpEmail(email)) {
            throw new ApiException(CompanyCode.ALREADY_REGISTERED_COMPANY);
        }
    }

    public CompanyEntity findByIdAndMatchCategoryWithThrow(long companyId, CompanyCategory companyCategory) {
        CompanyEntity entity = companyRepository.findById(companyId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        if (!entity.getCpCategory().equals(companyCategory)) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "업체 카테고리와 요청 카테고리가 다름");
        }

        return entity;
    }
}
