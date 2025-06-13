package aba3.lucid.company.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.company.converter.CompanyConverter;
import aba3.lucid.domain.company.dto.CompanyResponse;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyConverter companyConverter;

    public CompanyEntity findByIdAndMatchCategoryWithThrow(Long companyId, CompanyCategory companyCategory) {
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




    public CompanyEntity findByIdWithThrow(Long cpId) {
        log.info("findByIdWithThrow {}", cpId);
        return companyRepository.findById(cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }

    public CompanyEntity findByCpEmailWithThrow(String cpEmail) {
        return companyRepository.findByCpEmail(cpEmail)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }



    public CompanyEntity saveByCompany(CompanyEntity companyEntity) {
        return companyRepository.save(companyEntity);
    }

    // 업체 카테고리 가지고 오기
    public CompanyCategory getCategory(Long companyId) {
        return findByIdWithThrow(companyId).getCpCategory();
    }


    //업체 회원 목록 리스트 가져오기

    public List<CompanyResponse> findAllCompanies(Pageable pageable) {
        Page<CompanyEntity> companyPage = companyRepository.findAll(pageable);
        List<CompanyEntity> companyEntityList = companyRepository.findAll();
        List<CompanyResponse> companyResponseList = new ArrayList<>();
        for (CompanyEntity companyEntity : companyEntityList) {
            companyResponseList.add(companyConverter.toResponse(companyEntity));
        }
        return companyResponseList;
    }



}