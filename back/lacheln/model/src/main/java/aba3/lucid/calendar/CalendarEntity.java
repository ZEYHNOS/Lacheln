package aba3.lucid.calendar;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "calendar")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long calId;

    // 캘린더 날짜
    private LocalDate date;

    // 업체 id
    private long cpId;

}
