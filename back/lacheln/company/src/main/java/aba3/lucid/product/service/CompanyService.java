package aba3.lucid.product.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;


    // 카테고리가 같은지 확인하기
    public boolean matchCategory(long id, CompanyCategory category) {
        CompanyEntity entity = findByIdWithThrow(id);

        return entity.getCpCategory().equals(category);
    }

    // TODO 업체 에러 코드 만들기
    public CompanyEntity findByIdWithThrow(long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }

}
