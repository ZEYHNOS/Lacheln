package aba3.lucid.domain.packages.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PackageRegisterRequest {
    // 패키지 명
    @NotBlank
    private String packageName;

    // 패키지 설명
    @NotBlank
    private String packageComment;

    // 초대 업체 1
    @Email
    private String cpEmail1;

    // 초대 업체 2
    @Email
    private String cpEmail2;

}
