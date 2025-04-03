package aba3.lucid.domain.company.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyUpdateResponse implements Serializable {


    private String address;
}
