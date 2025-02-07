package aba3.lucid.notification.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "user_notice")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserNoticeEntity {

    @Id
    private  String alarmId;

    private  String userId;

    private String alertTitle;

    private String alertContent;

    private String alertSendTime;

    private String alertRead;


}
