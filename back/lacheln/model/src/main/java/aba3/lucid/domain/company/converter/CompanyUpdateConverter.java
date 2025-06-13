package aba3.lucid.domain.company.converter;


import aba3.lucid.domain.company.dto.CompanyUpdateRequest;
import aba3.lucid.domain.company.dto.CompanyUpdateResponse;
import aba3.lucid.domain.company.entity.CompanyEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanyUpdateConverter {

    public CompanyEntity toEntity(CompanyUpdateRequest updateRequest) {
        if(updateRequest == null) {
            return null;
        }
        return CompanyEntity.builder()
                .cpAddress(updateRequest.getAddress())
                .cpProfile(updateRequest.getProfileImg())
                .build();
    }

    public CompanyUpdateResponse toResponse(CompanyEntity companyEntity) {
        if(companyEntity == null) {
            return null;
        }
        return CompanyUpdateResponse.builder()
                .address(companyEntity.getCpAddress())
                .profileUrl(companyEntity.getCpProfile())
                .build();
    }
}
