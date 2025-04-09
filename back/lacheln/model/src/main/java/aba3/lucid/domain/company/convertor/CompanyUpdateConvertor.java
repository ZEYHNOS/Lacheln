package aba3.lucid.domain.company.convertor;


import aba3.lucid.domain.company.dto.CompanyUpdateRequest;
import aba3.lucid.domain.company.dto.CompanyUpdateResponse;
import aba3.lucid.domain.company.entity.CompanyEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanyUpdateConvertor {

    public CompanyEntity toEntity(CompanyUpdateRequest updateRequest) {
        if(updateRequest == null) {
            return null;
        }
        return CompanyEntity.builder()
                .cpPassword(updateRequest.getPassword())
                .cpAddress(updateRequest.getAddress())
                .build();
    }

    public CompanyUpdateResponse toResponse(CompanyEntity companyEntity) {
        if(companyEntity == null) {
            return null;
        }
        return CompanyUpdateResponse.builder()
                .address(companyEntity.getCpAddress())
                .build();
    }
}
