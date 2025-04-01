package aba3.lucid.domain.alert.dto;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.entity.PackageEntity;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CompanyAlertDto implements Serializable {

    private Long companyId;

    private String title;

    private String content;

    private LocalDateTime sentTime;

    private BinaryChoice isRead;

    private String accessUrl;


    public static CompanyAlertDto invitationPackage(CompanyEntity company, PackageEntity packageEntity) {
        String title = String.format("%s 패키지에 초대되었습니다.", packageEntity.getPackName());
        String content = String.format("%s님이 %s 패키지에 초대를 했습니다." +
                        "\n만약 원치않은 초대를 받았을 경우 나가기 및 신고 버튼을 눌러주세요."
                , packageEntity.getPackAdmin().getCpName()
                , packageEntity.getPackAdmin());
        String accessUrl = "/package/";

        return createAlert(company.getCpId(), title, content, accessUrl);
    }

    public static CompanyAlertDto test() {
        return createAlert(1L, "", "", "");
    }

    private static CompanyAlertDto createAlert(Long companyId, String title, String content, String accessUrl) {
        return CompanyAlertDto.builder()
                .companyId(companyId)
                .title(title)
                .content(content)
                .accessUrl(accessUrl)
                .isRead(BinaryChoice.N)
                .sentTime(LocalDateTime.now())
                .build()
                ;
    }

    private CompanyAlertDto createAlert(CompanyEntity company, String title, String content, String accessUrl) {
        return CompanyAlertDto.builder()
                .companyId(company.getCpId())
                .title(title)
                .content(content)
                .accessUrl(accessUrl)
                .isRead(BinaryChoice.N)
                .sentTime(LocalDateTime.now())
                .build()
                ;
    }

}
