package aba3.lucid.domain.calendar.entity;

import aba3.lucid.domain.company.entity.CompanyEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "calendar")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarEntity {

    // 캘린더 기본 ID
    @Id
    @Column(name = "cal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long calId;

    // 캘린더 날짜
    @Column(name = "cal_date", columnDefinition = "DATE", nullable = false)
    private LocalDate calDate;

    // 업체 외래키
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity company;

    // 캘린더 상세 List 받아오기
    @JsonIgnore
    @OneToMany(mappedBy= "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalendarDetailEntity> calendarDetailEntity = new ArrayList<>();

}
