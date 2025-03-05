package aba3.lucid.domain.calendar;

import aba3.lucid.domain.company.CompanyEntity;
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

    // ManyToOne 설정
    // 업체 id
    private long cpId;

    // 캘린더 날짜
    @Column(name = "cal_date", columnDefinition = "DATE", nullable = false)
    private LocalDate calDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity company;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "cal_dt_id")
    private CalendarDetailEntity calendarDetailEntity;


}
