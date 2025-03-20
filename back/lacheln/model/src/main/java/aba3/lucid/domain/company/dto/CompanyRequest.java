package aba3.lucid.domain.company.dto;

import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.enums.CompanyStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRequest implements CompanyRequestIfs{

    //@NotEmpty 는 null 과 "" 둘 다 허용하지 않게 하기 때문메 not empty 썼음.
    @Email(message = "이메일 형식 아닙니다")
    private String cpEmail;

    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해야 합니다.")
    private String cpPassword;

    //비밀번호 확인
    @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
    private String cpPasswordConfirm;

    @NotBlank
    private String cpName;

    @NotBlank
    private String cpRepName;

    @NotBlank(message = "대표자 전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{10,11}$",message = "전화번호는 10~11자리 숫자만 가능합니다.")
    private String cpMainContact;

    @NotBlank(message = "Role 필수 입력값입니다.")
    private String cpRole;

    @NotBlank(message = "우편번호 필수 입력값입니다.")
    private String cpPostalCode;

    @NotBlank(message = "사업자번호 필수 입력값입니다.")
    private String cpBnRegNo;

    @NotBlank(message = "통신판매업신고번호 필수 입력값입니다.")
    private String cpMos;

    @NotNull(message = "입점 상태 필수 입력값입니다 ")
    private CompanyStatus cpStatus;

    @NotBlank(message = "업체소개 이미지 필수 입력값입니다")
    private String cpProfile;

    private String cpExplain;

    @NotNull(message = "카테고리 필수 입력값입니다 ")
    private CompanyCategory cpCategory;

    private String cpContact;

    private String cpFax;

    //주소
    private String cpAddress;




}
