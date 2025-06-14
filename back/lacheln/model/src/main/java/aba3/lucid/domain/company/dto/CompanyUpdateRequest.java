package aba3.lucid.domain.company.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyUpdateRequest{

    private String profileImg;

    private String address;
}
