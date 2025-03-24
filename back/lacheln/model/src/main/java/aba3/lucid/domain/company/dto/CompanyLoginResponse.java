package aba3.lucid.domain.company.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyLoginResponse implements CompanyRequestIfs {
    private long cpId;
    private String cpEmail;
    private String cpName;
}
