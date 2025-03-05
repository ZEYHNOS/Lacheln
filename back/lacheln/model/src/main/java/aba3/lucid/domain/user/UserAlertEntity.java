package aba3.lucid.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Entity
@Table(name = "user_alert")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAlertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userAlertId;

    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
    private String user; //소비자ID 이메일+비밀번호+비밀키

    @Column(name = "user_alert", length = 50, nullable = false)
    private String userAlertTitle; //제목

    @Column(name = "user_alert_content", length = 100, nullable = false)
    private String userAlertContent; //내용

    @Column(name = "user_alert_sendtime", nullable = false)
    private LocalTime userAlertSendtime; //시간

    @Column(name = "user_alert_read", columnDefinition = "CHAR(1)", nullable = false)
    private String userAlertRead; //읽음여부

    @Column(name = "user_alert_url", length = 255, nullable = false)
    private String userAlertUrl; //접속URL
}
