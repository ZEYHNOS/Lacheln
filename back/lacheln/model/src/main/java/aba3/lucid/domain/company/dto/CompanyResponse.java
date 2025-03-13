package aba3.lucid.domain.company.dto;


import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponse implements Serializable {

    //todo list
    //CompanyResponse 만들기

    private Long cpId;

    private String cpEmail;

    private String cpName;

    private String cpRepName;

    private String cpMainContact;

    private String cpAddress;

    //Entity부터 Response DTO로 바꾸는 메서드
    public static CompanyResponse fromEntity(CompanyEntity company) {
        return CompanyResponse.builder()
                .cpId(company.getCpId())
                .cpEmail(company.getCpEmail())
                .cpName(company.getCpName())
                .cpRepName(company.getCpRepName())
                .cpMainContact(company.getCpMainContact())
                .cpAddress(company.getCpAddress())
                .build();
    }

}
