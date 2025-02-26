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
    private Long userAlertId;

    //소비자ID 이메일+비밀번호+비밀키
    private String userId;

    //제목
    private String userAlertTitle;

    //내용
    private String userAlertContent;

    //시간
    private LocalTime userAlertSendTime;

    //읽음여부
    private String userAlertRead;

    //접속URL
    private String userAlertUrl;
}
