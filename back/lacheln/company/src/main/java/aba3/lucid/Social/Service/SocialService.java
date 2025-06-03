package aba3.lucid.Social.Service;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.converter.SnsConverter;
import aba3.lucid.domain.company.dto.SnsRequest;
import aba3.lucid.domain.company.dto.SnsResponse;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.entity.SocialEntity;
import aba3.lucid.domain.company.enums.SNS;
import aba3.lucid.domain.company.repository.SocialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SocialService {
    private final SocialRepository socialRepository;
    private final SnsConverter snsConverter;
    private final CompanyService companyService;


    public SnsResponse createSocial(SnsRequest request, Long cpId) {
        CompanyEntity company = companyService.findByIdWithThrow(cpId);
        SNS SnsName = (request.getName() != null) ? (request.getName()): SNS.INSTAGRAM;
        String url = (request.getUrl() != null) ? (request.getUrl()): "www.instagram.com";

        SnsRequest defaultRequest = new SnsRequest(SnsName, url);
        SocialEntity entity = snsConverter.toEntity(defaultRequest,company);
        SocialEntity savedEntity = socialRepository.save(entity);
        return snsConverter.toResponse(savedEntity);
    }

    @Transactional
    public SnsResponse updateSocial(SnsRequest request, Long cpId) {
        SocialEntity socialEntity = socialRepository.findById(cpId).orElseThrow(
                () -> new ApiException(ErrorCode.NOT_FOUND, "해당 업체에 대한 SNS정보가 없습니다"+ cpId)
        );
        socialEntity.setSnsName(request.getName());
        socialEntity.setSnsUrl(request.getUrl());
        SocialEntity updatedEntity = socialRepository.save(socialEntity);
        return snsConverter.toResponse(updatedEntity);
    }

}
