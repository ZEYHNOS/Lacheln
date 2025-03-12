package aba3.lucid.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_exit")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserExitEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")
    private UsersEntity users;

    @CreationTimestamp
    @Column(name = "exit_date", nullable = false)
    private LocalDateTime exitDate; //탈퇴일

    @Column(name = "exit_reason", length = 100, nullable = false)
    private String exitReason; //탈퇴사유
}
