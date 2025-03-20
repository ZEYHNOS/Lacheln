package aba3.lucid.domain.company.dto;


import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.enums.CompanyStatus;
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

    private String cpRole;

    private String cpPostalCode;

    private String cpBnRegNo;

    private String cpMos;

    private CompanyStatus cpStatus;

    private String cpProfile;

    private String cpExplain;

    private CompanyCategory cpCategory;

    private String cpContact;

    private String cpFax;

}
