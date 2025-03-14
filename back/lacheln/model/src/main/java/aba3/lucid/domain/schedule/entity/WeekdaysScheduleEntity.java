package aba3.lucid.domain.schedule.entity;

import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.domain.company.entity.CompanyEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Entity
@Table(name = "weekdays_schedule")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeekdaysScheduleEntity {

    // 요일별 일정 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ws_id")
    private long wsId;

    // 업체
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity company;

    // 월요일~일요일
    @Enumerated(EnumType.STRING)
    @Column(name = "ws_weekdays", columnDefinition = "CHAR(3)", nullable = false)
    private Weekdays wsWeekdays;

    // 시작 시간
    @Column(name = "ws_start", columnDefinition = "DATETIME", nullable = false)
    private LocalTime wsStart;

    // 종료 시간
    @Column(name = "ws_end", columnDefinition = "DATETIME", nullable = false)
    private LocalTime wsEnd;
}
