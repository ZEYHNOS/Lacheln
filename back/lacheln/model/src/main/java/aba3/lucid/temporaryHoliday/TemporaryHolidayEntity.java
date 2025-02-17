package aba3.lucid.temporaryHoliday;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "temporary_holiday")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemporaryHolidayEntity {

    // 임시 휴일 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long temHolidayId;

    private long cpId;

    // 날짜
    private LocalDate hdDate;

    // 사유
    private String reason;

}
