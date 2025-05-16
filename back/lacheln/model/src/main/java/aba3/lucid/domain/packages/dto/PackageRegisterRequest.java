package aba3.lucid.domain.packages.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PackageRegisterRequest {

    // 패키지 명
    @NotBlank
    private String packageName;

    // 초대 업체 1
    @Email
    private String cpEmail1;

    // 초대 업체 2
    @Email
    private String cpEmail2;

}
