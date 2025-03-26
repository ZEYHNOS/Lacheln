package aba3.lucid.domain.company.dto;


import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.enums.CompanyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyProfileSetRequest implements CompanyRequestIfs {

    private String repName;

    @NotBlank(message = "대표자 전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{10,11}$",message = "전화번호는 10~11자리 숫자만 가능합니다.")
    private String mainContact;

    @NotBlank(message = "입점 상태 필수 입력값입니다 ")
    private CompanyStatus status;


    private String profile;

    private String explain;

    @NotBlank(message = "카테고리 필수 입력값입니다 ")
    private CompanyCategory category;

    private String fax;
}
