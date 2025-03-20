package aba3.lucid.domain.company.convertor;


import aba3.lucid.domain.company.dto.CompanyRequest;
import aba3.lucid.domain.company.dto.CompanyResponse;
import aba3.lucid.domain.company.entity.CompanyEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanyConvertor {

    public CompanyEntity toEntity(CompanyRequest request, String hashedPassword) {
        if(request == null) {
            return null;
        }
        //DTO -> Entity 바꾸기
        return CompanyEntity.builder()
                .cpEmail(request.getCpEmail())
                .cpPassword(hashedPassword)
                .cpName(request.getCpName())
                .cpRepName(request.getCpRepName())
                .cpMainContact(request.getCpMainContact())
                .cpAddress(request.getCpAddress())
                .cpRole(request.getCpRole())
                .cpPostalCode(request.getCpPostalCode())
                .cpBnRegNo(request.getCpBnRegNo())
                .cpMos(request.getCpMos())
                .cpStatus(request.getCpStatus())
                .cpProfile(request.getCpProfile())
                .cpExplain(request.getCpExplain())
                .cpCategory(request.getCpCategory())
                .cpContact(request.getCpContact())
                .cpFax(request.getCpFax())
                .build();
    }

    public CompanyResponse toResponse(CompanyEntity entity){
        if(entity == null) {
            return null;
        }

        return CompanyResponse.builder()
                .cpId(entity.getCpId())
                .cpEmail(entity.getCpEmail())
                .cpName(entity.getCpName())
                .cpRepName(entity.getCpRepName())
                .cpMainContact(entity.getCpMainContact())
                .cpAddress(entity.getCpAddress())
                .cpRole(entity.getCpRole())
                .cpPostalCode(entity.getCpPostalCode())
                .cpBnRegNo(entity.getCpBnRegNo())
                .cpMos(entity.getCpMos())
                .cpStatus(entity.getCpStatus())
                .cpProfile(entity.getCpProfile())
                .cpExplain(entity.getCpExplain())
                .cpCategory(entity.getCpCategory())
                .cpContact(entity.getCpContact())
                .cpFax(entity.getCpFax())
                .build();

    }





}
