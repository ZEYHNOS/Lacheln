package aba3.lucid.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Table(name="users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersEntity {
    @Id
    private String userId;

    private String userCountryId;

    private String userLanguage;

    private String currency;

    private String email;

    private String password;

    private String name;

    private String nickName;

    private String birthday;

    private String userSocial;

    private String phone;

    private String profile;

    private String tier;     //등급

    private String adsNotify;  //푸쉬 알림 여부

    private String accountStatus;  //마지막 접속

    private String joinDate;     //가입일

    private String modifyDate;  //수정 일

    private String accessTime;

    private String gender;

    private String mileage;
}
