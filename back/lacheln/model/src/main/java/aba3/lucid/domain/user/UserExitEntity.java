package aba3.lucid.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Entity
@Table(name = "user_exit")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserExitEntity {

    @Id
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    private String userId; //소비자ID FK

    @Column(name = "exit_date", nullable = false)
    private LocalDateTime exitDate; //탈퇴일

    @Column(name = "exit_reason", length = 100, nullable = false)
    private String exitReason; //탈퇴사유
}
