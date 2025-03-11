package aba3.lucid.dto.company;

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
    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식 아닙니다")
    private String cpEmail;

    @NotEmpty(message = "비밀번호는 필수 입력값입니다")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해야 합니다.")
    private String cpPassword;

    @NotEmpty
    private String cpName;

    @NotEmpty
    private String cpRepName;

    @NotEmpty(message = "대표자 전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{10,11}$",message = "전화번호는 10~11자리 숫자만 가능합니다.")
    private String cpMainContact;

}
