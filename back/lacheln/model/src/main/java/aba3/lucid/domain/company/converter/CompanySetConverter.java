package aba3.lucid.domain.company.converter;

import aba3.lucid.domain.company.dto.CompanyProfileSetRequest;
import aba3.lucid.domain.company.dto.CompanyProfileSetResponse;
import aba3.lucid.domain.company.entity.CompanyEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanySetConverter {

    public CompanyEntity toEntity(CompanyProfileSetRequest setRequest) {
        if(setRequest == null) {
            return null;
        }

        return CompanyEntity.builder()
                .cpRepName(setRequest.getRepName())
                .cpMainContact(setRequest.getMainContact())
                .cpStatus(setRequest.getStatus())
                .cpProfile(setRequest.getProfile())
                .cpExplain(setRequest.getExplain())
                .cpCategory(setRequest.getCategory())
                .cpFax(setRequest.getFax())
                .build();
    }


    public CompanyProfileSetResponse toResponse(CompanyEntity entity) {
        if(entity == null) {
            return null;
        }

        return  CompanyProfileSetResponse.builder()
                .repName(entity.getCpRepName())
                .mainContact(entity.getCpMainContact())
                        .status(entity.getCpStatus())
                .profile(entity.getCpProfile())
                .explain(entity.getCpExplain())
                .category(entity.getCpCategory())
                .fax(entity.getCpFax())
                .build();
    }




}
