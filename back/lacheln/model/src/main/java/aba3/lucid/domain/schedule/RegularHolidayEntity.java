package aba3.lucid.domain.schedule;

import aba3.lucid.enums.HolidayWeek;
import aba3.lucid.enums.Weekdays;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
    private long regHolidayId;

    // ManyToOne
    // 업체 id
    private long cpId;

    // 월요일~일요일
    @Enumerated(EnumType.STRING)
    @Column(name = "rh_hd_weekdays")
    private Weekdays rhHdWeekdays;

    // 격주 단위(1, 2, 3, 4, 5, 홀, 짝)
    @Enumerated(EnumType.STRING)
    @Column(name = "rh_hd_week")
    private HolidayWeek rhHdWeek;

    // 일 단위인지
    @Column(name = "rh_hd_days", columnDefinition = "INT")
    private int rhHdDays;

    // 월 단위인지
    @Column(name = "rh_hd_month", columnDefinition = "INT")
    private int rhHdMonth;

}
