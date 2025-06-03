package aba3.lucid.company.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.image.ImageType;
import aba3.lucid.common.status_code.CompanyCode;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.converter.CompanyConverter;
import aba3.lucid.domain.company.converter.CompanySetConverter;
import aba3.lucid.domain.company.converter.CompanyUpdateConverter;
import aba3.lucid.domain.company.dto.*;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.enums.CompanyStatus;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;



@Slf4j
@Business
@RequiredArgsConstructor
public class CompanyBusiness {
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final CompanyConverter companyConverter;
    private final CompanyUpdateConverter companyUpdateConverter;
    private final ImageService imageService;

    private String serviceKey;

    private final CompanySetConverter companySetConverter;


    //passwordEncoder.encode(rawPassword)를 호출하여 비밀번호를 암호화합니다.

    public CompanyResponse registerCompany(CompanyRequest request) {
        if(request == null) {
            throw  new ApiException(ErrorCode.BAD_REQUEST, "CompanyRequest 값을 받지 못했습니다");
        }

        if(!request.getPassword().equals(request.getPasswordConfirm())) {
            throw  new ApiException(ErrorCode.BAD_REQUEST, "비밀번호와 비밀번호 확인 값이 일치하지 않습니다");
        }

        validateDuplicateCompany(request.getEmail());

        CompanyEntity companyEntity = companyConverter.toEntity(request);

        companyEntity.setCpRepName("임시대표");
        companyEntity.setCpMainContact("01000000000");
        companyEntity.setCpStatus(CompanyStatus.SUSPENSION);
        companyEntity.setCpProfile("default.png");
        companyEntity.setCpCategory(CompanyCategory.S);


        CompanyEntity savedEntity = companyRepository.save(companyEntity);
        return companyConverter.toResponse(savedEntity);
    }

    public CompanyProfileSetResponse setCompanyProfile(Long companyId,  CompanyProfileSetRequest request,MultipartFile profileImgFile) {
        CompanyEntity entity = findByIdWithThrow(companyId);

        if(profileImgFile != null && !profileImgFile.isEmpty()) {
            String profileImageUrl  = imageService.savedProfileImages(companyId,profileImgFile,ImageType.PROFILE);
            entity.setCpProfile(profileImageUrl);
        }

        entity.setCpRepName(request.getRepName());
        entity.setCpMainContact(request.getMainContact());
        entity.setCpStatus(request.getStatus());
        entity.setCpExplain(request.getExplain());
        entity.setCpCategory(request.getCategory());
        entity.setCpFax(request.getFax());

        CompanyEntity updated = companyRepository.save(entity);
        return companySetConverter.toResponse(updated);
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




    public API<CompanyUpdateResponse> updateCompany(CompanyUpdateRequest companyUpdateRequest, Long companyId,MultipartFile profileImgFile) {

        if(companyUpdateRequest == null) {
            throw new ApiException(ErrorCode.NOT_FOUND, "요청에 대한 정보가 없ㅅ습니다");
        }
        CompanyEntity loadCompany = companyService.findByIdWithThrow(AuthUtil.getCompanyId());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String oldProfilePath = loadCompany.getCpProfile();
        if(profileImgFile != null && !profileImgFile.isEmpty()) {
            //기존 이미지를 있다면 파일 삭제
            if(oldProfilePath != null && !oldProfilePath.isBlank()) {
                imageService.deleteProfileImage(oldProfilePath);
            }
            String updatedProfileImageUrl = imageService.savedProfileImages(companyId,profileImgFile,ImageType.PROFILE);
            loadCompany.setCpProfile(updatedProfileImageUrl);
        }
        loadCompany.updateCompany(companyUpdateRequest,bCryptPasswordEncoder);
        companyService.saveByCompany(loadCompany);
        CompanyUpdateResponse data = CompanyUpdateResponse.builder()
                .address(loadCompany.getCpAddress())
                .profile(loadCompany.getCpProfile())
                .build();
        return API.OK(data);

    }

    public CompanyResponse searchCompany(String email) {
        Optional<CompanyEntity> companyOpt = companyRepository.findByCpEmail(email);
        if(companyOpt.isPresent()) {
            CompanyEntity company = companyOpt.get();
            return companyConverter.toResponse(company);
        }else {
            throw new ApiException(ErrorCode.NOT_FOUND, "회사를 찾을 수 앖습니다");
        }
    }

    public CompanyCategory getCategory(Long companyId) {
        Validator.throwIfInvalidId(companyId);

        return companyService.getCategory(companyId);
    }




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