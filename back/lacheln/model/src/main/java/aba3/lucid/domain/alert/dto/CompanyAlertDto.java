package aba3.lucid.domain.alert.dto;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.alert.enums.AlertType;
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

    private Long alertId;

    private Long companyId;

    private String type;

    private String text;

    private LocalDateTime sentTime;

    private BinaryChoice isRead;

    private String accessUrl;

    public static CompanyAlertDto invitationPackage(CompanyEntity company, PackageEntity entity) {
        return CompanyAlertDto.builder()
                .companyId(company.getCpId())
                .type(AlertType.INVITATION_PACKAGE.getType())
                .text(String.format(AlertType.INVITATION_PACKAGE.getText(), company.getCpName(), entity.getPackName()))
                .sentTime(LocalDateTime.now())
                .isRead(BinaryChoice.N)
                .accessUrl(String.format(AlertType.INVITATION_PACKAGE.getBaseUrl(), entity.getPackId()))
                .build()
                ;
    }
}
