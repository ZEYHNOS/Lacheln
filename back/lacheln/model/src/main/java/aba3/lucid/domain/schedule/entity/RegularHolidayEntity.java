package aba3.lucid.domain.schedule.entity;

import aba3.lucid.common.enums.HolidayWeek;
import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.domain.company.entity.CompanyEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "regular_holiday")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegularHolidayEntity {

    // 정기 휴무일 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regular_holiday")
    private Long regHolidayId;

    // ManyToOne
    @JoinColumn(name = "cp_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CompanyEntity company;

    // 요일
    @Enumerated(EnumType.STRING)
    @Column(name = "rh_hd_weekdays", columnDefinition = "CHAR(3)")
    private Weekdays rhHdWeekdays;

    // 격주 단위(1, 2, 3, 4, 5, 홀, 짝)
    @Enumerated(EnumType.STRING)
    @Column(name = "rh_hd_week", columnDefinition = "VARCHAR(10)")
    private HolidayWeek rhHdWeek;

    // 일 단위인지
    @Column(name = "rh_hd_days", columnDefinition = "INT")
    private Integer rhHdDays;

    // 월 단위인지
    @Column(name = "rh_hd_month", columnDefinition = "INT")
    private Integer rhHdMonth;

}
