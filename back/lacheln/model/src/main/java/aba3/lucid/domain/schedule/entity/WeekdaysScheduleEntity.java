package aba3.lucid.domain.schedule.entity;

import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Entity
@Setter
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
    @Column(name = "ws_weekdays", columnDefinition = "CHAR(10)", nullable = false)
    private Weekdays wsWeekdays;

    // 시작 시간
    @Column(name = "ws_start", columnDefinition = "TIME", nullable = false)
    private LocalTime wsStart;

    // 종료 시간
    //“오픈/마감 시간”만 필요하다면 DB 컬럼 타입을 TIME으로 변경하는 것이 자연스럽습니다.
    @Column(name = "ws_end", columnDefinition = "TIME", nullable = false)
    private LocalTime wsEnd;




}
