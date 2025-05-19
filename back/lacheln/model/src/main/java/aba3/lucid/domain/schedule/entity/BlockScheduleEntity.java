package aba3.lucid.domain.schedule.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="blockschedule")
public class BlockScheduleEntity {

    // 스케쥴 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blockScheduleId;

    // 상품 ID
    @Column(name = "pd_id")
    private Long pdId;

    // 시작 시간
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    // 종료 시간
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
}
