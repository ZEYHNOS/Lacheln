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

    private String title;

    private String content;

    private LocalDateTime sentTime;

    private BinaryChoice isRead;

    private String accessUrl;


    public static MutualAlert createCompanyAlert(String title, String content, String accessUrl, Long companyId, String userId) {
        return MutualAlert.builder()
                .companyId(companyId)
                .userId(userId)
                .title(title)
                .content(content)
                .accessUrl(accessUrl)
                .isRead(BinaryChoice.N)
                .sentTime(LocalDateTime.now())
                .build()
                ;
    }

    public static MutualAlert createCompanyAlert(String title, String content, Long companyId, String userId) {
        return MutualAlert.builder()
                .companyId(companyId)
                .userId(userId)
                .title(title)
                .content(content)
                .accessUrl(null)
                .isRead(BinaryChoice.N)
                .sentTime(LocalDateTime.now())
                .build()
                ;
    }

}
