package aba3.lucid.domain.alert.dto;

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

    private Long alertId;

    private String userId;

    private String title;

    private String content;

    private LocalDateTime sentTime;

    private BinaryChoice isRead;

    private String accessUrl;

    public static UserAlertDto reviewCommentAlert(String userId) {
        return UserAlertDto.builder()
                .userId(userId)
                .title("리뷰 답글 작성")
                .content("리뷰 답글이 작성되었습니다.")
                .sentTime(LocalDateTime.now())
                .isRead(BinaryChoice.N)
                .accessUrl("/user/review")
                .build()
                ;
    }

    public static UserAlertDto upgradeRankUp() {
        return UserAlertDto.builder()
                .title("Tier Up")
                .content("티어가 올라갔습니다.")
                .sentTime(LocalDateTime.now())
                .accessUrl("/user")
                .isRead(BinaryChoice.N)
                .build()
                ;
    }
}
