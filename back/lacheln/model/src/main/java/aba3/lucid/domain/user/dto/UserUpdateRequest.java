package aba3.lucid.domain.user.dto;

import aba3.lucid.domain.user.enums.CurrencyEnum;
import aba3.lucid.domain.user.enums.GenderEnum;
import aba3.lucid.domain.user.enums.LanguageEnum;
import aba3.lucid.domain.user.enums.NotificationEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    private String name;

    private String password;

    @NotBlank(message = "닉네임은 필수 입력값 입니다.")
    @Size(min = 2, max = 15, message = "닉네임은 2~15자로 입력해야 합니다.")
    private String nickname;

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호 양식을 맞춰주세요")
    private String phone;

    private LanguageEnum language;

    private CurrencyEnum currency;

    private String image;

    private NotificationEnum notification;

    private GenderEnum gender;
}
