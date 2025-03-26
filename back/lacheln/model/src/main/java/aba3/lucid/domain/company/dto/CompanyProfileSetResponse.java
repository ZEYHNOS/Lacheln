package aba3.lucid.domain.company.dto;

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
public class CompanyProfileSetResponse implements Serializable {


    private String repName;

    private String mainContact;

    private CompanyStatus status;

    private String profile;

    private String explain;

    private CompanyCategory category;

    private String fax;
}
