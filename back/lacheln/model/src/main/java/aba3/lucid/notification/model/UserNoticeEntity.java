package aba3.lucid.notification.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO 샤히드

@Getter
@Entity
@Table(name = "user_notice")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserNoticeEntity {

    /**
     * TODO space 관리 잘하기
     * TODO alarmId 제외하고 alarm 빼기
     */
    @Id
    private  String alarmId;

    private  String userId;

    private String alertTitle;

    private String alertContent;

    private String alertSendTime;

    private String alertRead;


}
