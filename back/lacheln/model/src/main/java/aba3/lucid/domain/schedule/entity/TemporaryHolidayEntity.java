package aba3.lucid.domain.schedule.entity;

import aba3.lucid.domain.company.entity.CompanyEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "temporary_holiday")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemporaryHolidayEntity {

    // 임시 휴일 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long temHolidayId;

    // 업체
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity company;

    // 휴무일
    @Column(name = "th_date", columnDefinition = "DATE", nullable = false)
    private LocalDate thDate;

    // 사유
    @Column(name = "th_reason", columnDefinition = "VARCHAR(255)")
    private String thReason;

}
