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
        //Entity -> DTO 바꾸기
        return CompanyEntity.builder()
                .cpEmail(request.getEmail())
                .cpPassword(hashedPassword)
                .cpName(request.getName())
                .cpRepName(request.getRepName())
                .cpMainContact(request.getMainContact())
                .cpAddress(request.getAddress())
                .cpRole(request.getRole())
                .cpPostalCode(request.getPostalCode())
                .cpBnRegNo(request.getBnRegNo())
                .cpMos(request.getMos())
                .cpStatus(request.getStatus())
                .cpProfile(request.getProfile())
                .cpExplain(request.getExplain())
                .cpCategory(request.getCategory())
                .cpContact(request.getContact())
                .cpFax(request.getFax())
                .build();
    }

    public CompanyResponse toResponse(CompanyEntity entity){
        if(entity == null) {
            return null;
        }

        return CompanyResponse.builder()
                .id(entity.getCpId())
                .email(entity.getCpEmail())
                .name(entity.getCpName())
                .repName(entity.getCpRepName())
                .mainContact(entity.getCpMainContact())
                .address(entity.getCpAddress())
                .role(entity.getCpRole())
                .postalCode(entity.getCpPostalCode())
                .bnRegNo(entity.getCpBnRegNo())
                .mos(entity.getCpMos())
                .status(entity.getCpStatus())
                .profile(entity.getCpProfile())
                .explain(entity.getCpExplain())
                .category(entity.getCpCategory())
                .contact(entity.getCpContact())
                .fax(entity.getCpFax())
                .build();

    }





}
