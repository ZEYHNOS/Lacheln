package aba3.lucid.regularHoliday;

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
    private long regHolidayId;

    // 업체 id
    private long cpId;

    // 월요일~일요일
    @Enumerated(EnumType.STRING)
    private Weekdays weekdays;

    // 격주 단위(1, 2, 3, 4, 5, 홀, 짝)
    @Enumerated(EnumType.STRING)
    private HolidayWeek holidayWeek;

    // 월 단위인지
    // TODO 도메인 설정하기, NULL 허용
    private int month;

    // 연 단위인지
    // TODO 도메인 설정하기, NULL 허용
    private int year;

}
