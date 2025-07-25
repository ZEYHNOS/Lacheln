package aba3.lucid.domain.company.dto;

import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.enums.CompanyStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CompanyRequest {

    //@NotEmpty 는 null 과 "" 둘 다 허용하지 않게 하기 때문메 not empty 썼음.
    @Email(message = "이메일 형식 아닙니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해야 합니다.")
    private String password;

    //비밀번호 확인
    @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
    private String passwordConfirm;

    @NotBlank
    private String name;


    private String postalCode;

    @NotBlank(message = "사업자번호 필수 입력값입니다.")
    @Pattern( regexp = "^\\d{3}-\\d{2}-\\d{5}$",
    message = "사업자등록번호 현식은 'xxx-xx-xxxxx'로 입력해야 합니다.")
    private String bnRegNo;

    @NotBlank(message = "통신판매업신고번호 필수 입력값입니다.")
    @Pattern(
            regexp = "^\\d{4}-[\\uAC00-\\uD7A3]+-\\d{5}$",
            message = "통신판매업신고번호 형식은 '2019-서울강남-01234'로 입력해야 합니다."
    )
    private String mos;



    private String contact;

    //주소
    private String address;

    private CompanyCategory category;




}
