package aba3.lucid.temporaryHoliday;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    // ManyToOne
    private long cpId;

    // 날짜
    @Column(name = "th_date", columnDefinition = "DATE", nullable = false)
    private LocalDate thDate;

    // 사유
    @Column(name = "th_reason", columnDefinition = "VARCHAR(255)")
    private String thReason;

}
