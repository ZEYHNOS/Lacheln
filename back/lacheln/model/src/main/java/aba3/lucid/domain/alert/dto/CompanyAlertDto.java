package aba3.lucid.domain.alert.dto;

import aba3.lucid.common.enums.AlertType;
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


    // 패키지에 업체 초대 알림
    public static CompanyAlertDto invitationPackage(CompanyEntity company, PackageEntity packageEntity) {
        AlertType alert = AlertType.INVITATION_PACKAGE;

        String title = alert.getTitle();
        String content = String.format(alert.getContent(), company.getCpName(), packageEntity.getPackName());
        String accessUrl = String.format(alert.getAccessUrl(), packageEntity.getPackId());

        return createAlert(company.getCpId(), title, content, accessUrl);
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
