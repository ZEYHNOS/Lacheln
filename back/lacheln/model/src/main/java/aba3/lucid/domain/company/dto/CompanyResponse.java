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

    private Long id;

    private String email;

    private String name;


    private String address;

    private String role;

    private String postalCode;

    private String bnRegNo;

    private String mos;

    private String contact;


}
