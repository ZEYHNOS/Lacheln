package aba3.lucid.domain.alert.dto;

import aba3.lucid.common.enums.BinaryChoice;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MutualAlert {

    private Long companyId;

    private String userId;

    private String type;

    private String text;

    private LocalDateTime sentTime;

    private BinaryChoice isRead;

    private String accessUrl;


    public static MutualAlert createCompanyAlert(String type, String text, String accessUrl, Long companyId, String userId) {
        return MutualAlert.builder()
                .companyId(companyId)
                .userId(userId)
                .type(type)
                .text(text)
                .accessUrl(accessUrl)
                .isRead(BinaryChoice.N)
                .sentTime(LocalDateTime.now())
                .build()
                ;
    }

    public static MutualAlert createCompanyAlert(String type, String text, Long companyId, String userId) {
        return MutualAlert.builder()
                .companyId(companyId)
                .userId(userId)
                .type(type)
                .text(text)
                .accessUrl(null)
                .isRead(BinaryChoice.N)
                .sentTime(LocalDateTime.now())
                .build()
                ;
    }

}
