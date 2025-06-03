package aba3.lucid.domain.alert.dto;

import aba3.lucid.common.enums.AlertType;
import aba3.lucid.common.enums.BinaryChoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAlertDto {

    private String userId;

    private String title;

    private String content;

    private LocalDateTime sentTime;

    private BinaryChoice isRead;

    private String accessUrl;

    public static UserAlertDto createAlert(String userId, AlertType type) {
        return UserAlertDto.builder()
                .userId(userId)
                .title(type.getTitle())
                .content(String.format(type.getContent(), userId))
                .accessUrl(type.getAccessUrl())
                .sentTime(LocalDateTime.now())
                .isRead(BinaryChoice.N)
                .build()
                ;
    }

}
