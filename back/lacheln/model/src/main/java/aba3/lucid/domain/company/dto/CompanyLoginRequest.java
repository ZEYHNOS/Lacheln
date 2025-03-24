package aba3.lucid.domain.company.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyLoginRequest implements CompanyRequestIfs {
    @Email(message = "이메일 형식이 아닙니다")
    private String cpEmail;

    @NotBlank(message = "비밀번호 필수 입력값입니다")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해야 합니다.")
    private String cpPassword;




}
