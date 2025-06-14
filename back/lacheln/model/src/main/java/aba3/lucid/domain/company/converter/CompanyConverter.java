package aba3.lucid.domain.company.converter;


import aba3.lucid.domain.company.dto.CompanyRequest;
import aba3.lucid.domain.company.dto.CompanyResponse;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.enums.CompanyStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CompanyConverter {

    private final BCryptPasswordEncoder passwordEncoder;

    public CompanyEntity toEntity(CompanyRequest request) {
        if(request == null) {
            return null;
        }
        //Entity -> DTO 바꾸기
        return CompanyEntity.builder()
                .cpEmail(request.getEmail())
                .cpPassword(passwordEncoder.encode(request.getPassword()))
                .cpName(request.getName())
                .cpAddress(request.getAddress())
                .cpPostalCode(request.getPostalCode())
                .cpBnRegNo(request.getBnRegNo())
                .cpMos(request.getMos())
                .cpContact(request.getContact())
                .cpRepName("임시대표")
                .cpMainContact("01055569887")
                .cpStatus(CompanyStatus.SUSPENSION)
                .cpProfile("default.png")
                .cpCategory(CompanyCategory.S)
                .cpRole("COMPANY")
                .build();
    }

    public CompanyResponse toResponse(CompanyEntity entity){
        if(entity == null) {
            return null;
        }

        return CompanyResponse.builder()
                .id(entity.getCpId())
                .companyJoinDate(entity.getCompanyJoinDate())
                .email(entity.getCpEmail())
                .name(entity.getCpName())
                .address(entity.getCpAddress())
                .postalCode(entity.getCpPostalCode())
                .bnRegNo(entity.getCpBnRegNo())
                .mos(entity.getCpMos())
                .contact(entity.getCpContact())
                .category(entity.getCpCategory())
                .profileImageUrl(entity.getCpProfile())
                .build();

    }






}