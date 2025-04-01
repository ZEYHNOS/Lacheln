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
import lombok.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CompanyBusiness {
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final CompanyConvertor companyConvertor;

    private String serviceKey;

    //ApplicationContext를 생성자의 의존성 주입을 통해 받아오고 있습니다:
    private final ApplicationContext applicationContext;
    private final CompanySetConvertor companySetConvertor;

    public CompanyBusiness(CompanyService companyService,
                           CompanyRepository companyRepository,
                           CompanyConvertor companyConvertor,
                           ApplicationContext applicationContext,
                           CompanySetConvertor companySetConvertor)
    {

        this.companyService = companyService;
        this.companyRepository = companyRepository;
        this.companyConvertor = companyConvertor;
        this.applicationContext = applicationContext;
        this.companySetConvertor = companySetConvertor;
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

//        CompanyEntity savedCompanyEntity = companyConvertor.toEntity(request, hashedPassword);
//        CompanyEntity savedCompanyEntitySaved = companyRepository.save(savedCompanyEntity);
        CompanyEntity companyEntity = companyConvertor.toEntity(request);

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

    public CompanyResponse searchCompany(String email) {
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



//    public Map<String, Object> checkBusinessStatus(String bNo) {
//        // bNo: 조회할 사업자등록번호
//        if (bNo == null || bNo.trim().isEmpty()) {
//            throw new ApiException(ErrorCode.BAD_REQUEST, "사업자등록번호가 유효하지 않습니다.");
//        }
//
//        Map<String, Object> result = new HashMap<>();
//        try {
//            // 1) RestTemplate 생성
//            RestTemplate restTemplate = new RestTemplate();
//
//            // 2) serviceKey 디코딩 (중복 인코딩 방지)
//            String decodedServiceKey = URLDecoder.decode(serviceKey, StandardCharsets.UTF_8.name());
//
//            // 3) API 요청 URL
//            //    serviceKey는 query param으로 전달
//            String url = "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=" + decodedServiceKey;
//
//            // 4) HTTP Header 세팅
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//            // 5) 요청 Body(b_no 배열 형태)
//            Map<String, Object> requestBody = new HashMap<>();
//            requestBody.put("b_no", Collections.singletonList(bNo));
//
//            // 6) HttpEntity 생성
//            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
//
//            // 7) API 호출 (POST)
//            ResponseEntity<Map> response = restTemplate.exchange(
//                    url,
//                    HttpMethod.POST,
//                    entity,
//                    Map.class
//            );
//
//            // 8) 응답 파싱
//            result = response.getBody(); // JSON -> Map 형태로 매핑됨
//            if (result == null) {
//                throw new ApiException(ErrorCode.NOT_FOUND, "OpenAPI 응답이 비어있습니다.");
//            }
//
//            return result;
//
//        } catch (HttpClientErrorException e) {
//            // 4xx, 5xx 에러 등
//            throw new ApiException(ErrorCode.NOT_FOUND, "OpenAPI 호출 중 오류: " + e.getResponseBodyAsString());
//        } catch (UnsupportedEncodingException e) {
//            throw new ApiException(ErrorCode.NOT_FOUND, "serviceKey 디코딩 오류: " + e.getMessage());
//        }
//    }



}