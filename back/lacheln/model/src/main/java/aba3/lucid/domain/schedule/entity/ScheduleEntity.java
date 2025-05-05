package aba3.lucid.domain.schedule.entity;


import aba3.lucid.common.enums.Schedules;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="schedule")
public class ScheduleEntity {
    // 스케쥴 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long scheduleId;

    // 업체 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity company;

    // 결제 상세 ID
    @Column(name = "pay_dt_id", nullable = false)
    private Long payDetailId;

    //일정 상태:예정, 중, 완료, 취소
    @Enumerated(EnumType.STRING)
    @Column(name = "sch_status", columnDefinition = "CHAR(20)", nullable = false)
    private Schedules schStatus;

    //일정 날짜
    @Column(name = "sch_date", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime schDate;

    // 총 소요시간
    @Column(name = "sch_total_time", columnDefinition = "INT", nullable = false)
    private int schTotalTime;
}
