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
import aba3.lucid.domain.company.converter.CompanyPasswordConverter;
import aba3.lucid.domain.company.converter.CompanySetConverter;
import aba3.lucid.domain.company.converter.CompanyUpdateConverter;
import aba3.lucid.domain.company.dto.*;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.enums.CompanyStatus;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.user.dto.UserAllResponse;
import aba3.lucid.domain.user.dto.UserInfoDto;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
    private final BCryptPasswordEncoder passwordEncoder;
    private final CompanyPasswordConverter companyPasswordConverter;

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

    public CompanyProfileSetResponse setCompanyProfile(Long companyId,  CompanyProfileSetRequest request,MultipartFile profileImgFile) throws IOException {
        CompanyEntity entity = findByIdWithThrow(companyId);

        if(profileImgFile != null && !profileImgFile.isEmpty()) {
            String profileImageUrl  = imageService.profileImageUpload(entity,profileImgFile, ImageType.PROFILE);
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




    public CompanyUpdateResponse updateCompany( Long companyId,MultipartFile profileImgFile, CompanyUpdateRequest companyUpdateRequest) throws IOException {

        if(companyUpdateRequest == null) {
            throw new ApiException(ErrorCode.NOT_FOUND, "요청에 대한 정보가 없ㅅ습니다");
        }
        CompanyEntity loadCompany = companyService.findByIdWithThrow(AuthUtil.getCompanyId());
        handleProfileImage(loadCompany,companyId,profileImgFile);
        loadCompany.setCpAddress(companyUpdateRequest.getAddress());
        loadCompany.setCpProfile(companyUpdateRequest.getProfileImg());
        companyService.saveByCompany(loadCompany);
        CompanyUpdateResponse data = CompanyUpdateResponse.builder()
                .id(loadCompany.getCpId())
                .address(loadCompany.getCpAddress())
                .profileUrl(loadCompany.getCpProfile())
                .build();
        return  data;

    }


    private void handleProfileImage(CompanyEntity company, Long companyId, MultipartFile profileImgFile) throws IOException {
        String oldProfilePath = company.getCpProfile();
        if (profileImgFile != null && !profileImgFile.isEmpty()) {
            // 기존 이미지 삭제
            if (oldProfilePath != null && !oldProfilePath.isBlank()) {
                imageService.deleteProfileImage(oldProfilePath);
            }
            // 새 이미지 저장
            String updatedProfileImageUrl = imageService.profileImageUpload(company, profileImgFile, ImageType.PROFILE);
            company.setCpProfile(updatedProfileImageUrl);
        }
    }


    public CompanyPasswordUpdateResponse updatePassword(CompanyPasswordUpdateRequest request, Long cpId) {
        Validator.throwIfInvalidId(cpId);
        Validator.throwIfNull(request);

        CompanyEntity company = companyService.findByIdWithThrow(cpId);
        // 1. 현재 비밀번호 일치 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), company.getCpPassword())) {
            throw new ApiException(ErrorCode.NOT_FOUND , "이전 비밀번호는 맞지 않습니다");
        }
        // 2. 새 비밀번호로 업데이트
        company.setCpPassword(passwordEncoder.encode(request.getNewPassword()));
        CompanyEntity updated = companyRepository.save(company);
        return companyPasswordConverter.toResponse(updated);

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

    public CompanyResponse getCompanyInfo(Long id) {
        CompanyEntity company = findByIdWithThrow(id);
        return companyConverter.toResponse(company);
    }

    public CompanyUpdateResponse getUrlList(Long companyId) {
        Validator.throwIfInvalidId(companyId);
        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        CompanyUpdateResponse data = CompanyUpdateResponse.builder()
                .id(company.getCpId())
                .address(company.getCpAddress())
                .profileUrl(company.getCpProfile())   // 이미지 URL 반환!
                .build();
        return data;
    }

    //모든 업체 정보
    public API<CompanyAllResponse<CompanyResponse>> getAllCompanies(Integer pageNum) {
        Pageable page = PageRequest.of(pageNum, 10);
        Page<CompanyEntity> companies = companyService.findAll(page);

        List<CompanyResponse> companyResponseList = companies.getContent()
                .stream()
                .map(companyConverter:: toResponse)
                .toList();

        CompanyAllResponse<CompanyResponse> responses = CompanyAllResponse.<CompanyResponse>builder()
                .companies(companyResponseList)
                .last(companies.isLast())
                .size(companies.getSize())
                .totalElements(companies.getTotalElements())
                .totalPages(companies.getTotalPages())
                .page(companies.getNumber())
                .build();

        return API.OK(responses);
    }

    // CompanyBusiness
    public API<Long> getTodayNewCompanyCount() {
        long count = companyService.getTodayNewCompanyCount();
        return API.OK(count);
    }

    public API<List<Object[]>> getMonthlyJoinStats() {
        List<Object[]> stats = companyService.getMonthlyJoinCount();
        return API.OK(stats);
    }





}