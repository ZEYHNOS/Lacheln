package aba3.lucid.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Entity
@Table(name = "user_exit")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserExitEntity {

    @Id
    private String userId;

    //탈퇴일
    private LocalTime exitDate;

    //탈퇴 사유
    private String reason;
}
