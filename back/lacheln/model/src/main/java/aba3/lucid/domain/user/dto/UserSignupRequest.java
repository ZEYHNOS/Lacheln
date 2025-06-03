package aba3.lucid.domain.user.dto;

import aba3.lucid.domain.user.enums.GenderEnum;
import aba3.lucid.domain.user.enums.NotificationEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Converter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserSignupRequest {
    // 이메일
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    // 비밀번호
    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해야 합니다.")
    private String password;

    // 비밀번호 확인
    @NotBlank(message = "비밀번호 확인은 필수 입력값입니다")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해야 합니다.")
    private String confirmPassword;

    // 이름
    @NotBlank
    private String name;

    // 닉네임
    @NotBlank
    private String nickName;

    // 전화번호
    private String phone;

    // 생일
    @NotBlank
    private LocalDate birthDate;

    // 성별
    @NotBlank
    private GenderEnum gender;

    // 푸쉬 알람 여부
    private NotificationEnum adsNotification;

    // 전화번호 인증여부
    private boolean isPhoneVerified;

    // 이메일 인증여부
    private boolean isEmailVerified;
}
