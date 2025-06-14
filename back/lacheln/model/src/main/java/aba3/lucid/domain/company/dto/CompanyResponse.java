package aba3.lucid.domain.company.dto;


import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.enums.CompanyStatus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


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


    private String postalCode;

    private String bnRegNo;

    private String mos;

    private String contact;

    private CompanyCategory category;

    private String profileImageUrl;

    private LocalDateTime companyJoinDate;

}
