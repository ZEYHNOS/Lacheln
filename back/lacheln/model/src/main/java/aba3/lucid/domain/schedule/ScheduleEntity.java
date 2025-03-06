package aba3.lucid.domain.schedule;


import aba3.lucid.common.enums.Schedules;
import aba3.lucid.domain.company.CompanyEntity;
import aba3.lucid.domain.product.ProductEntity;
import aba3.lucid.domain.user.UsersEntity;
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
    private CompanyEntity companyId;

    // 사용자 ID
    @OneToOne
    @JoinColumn(name = "user_id")
    private UsersEntity userId;

    // 상품 ID
    @OneToOne
    @JoinColumn(name = "pd_id")
    private ProductEntity productId;

    //일정 상태:예정, 중, 완료, 취소
    @Enumerated(EnumType.STRING)
    @Column(name = "sch_status", columnDefinition = "CHAR(20)", nullable = false)
    private Schedules schStatus;

    //일정 날짜
    @Column(name = "sch_date", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime schDate;

    //특이사항
    @Column(name = "sch_details", columnDefinition = "VARCHAR(255)")
    private String schDetails;

    // 인원
    @Column(name = "sch_person", columnDefinition = "INT", nullable = false)
    private int schPerson;

    // 총 소요시간
    @Column(name = "sch_total_time", columnDefinition = "INT", nullable = false)
    private int schTotalTime;
}
