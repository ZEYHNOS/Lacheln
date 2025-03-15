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
                .build();

    }





}
