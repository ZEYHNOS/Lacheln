package aba3.lucid.domain.user.entity;

import aba3.lucid.common.enums.BinaryChoice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    @ManyToOne
    private UsersEntity users;

    @Column(name = "user_alert", length = 50, nullable = false)
    private String userAlertTitle; //제목

    @Column(name = "user_alert_content", length = 100, nullable = false)
    private String userAlertContent; //내용

    @CreationTimestamp
    @Column(name = "user_alert_sendtime", nullable = false)
    private LocalDateTime userAlertSendtime; //시간

    @Enumerated(EnumType.STRING)
    @Column(name = "user_alert_read", columnDefinition = "CHAR(1)", nullable = false)
    private BinaryChoice userAlertRead; //읽음여부

    @Column(name = "user_alert_url", length = 255, nullable = false)
    private String userAlertUrl; //접속URL
}
