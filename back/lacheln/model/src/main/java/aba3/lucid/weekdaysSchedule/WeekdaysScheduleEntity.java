package aba3.lucid.weekdaysSchedule;

import aba3.lucid.enums.Weekdays;
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
    private long weekSchId;

    private long cpId;

    // 월요일~일요일
    @Enumerated(EnumType.STRING)
    private Weekdays weekdays;

    // 시작 시간
    private LocalTime start;

    // 종료 시간
    private LocalTime end;

}
