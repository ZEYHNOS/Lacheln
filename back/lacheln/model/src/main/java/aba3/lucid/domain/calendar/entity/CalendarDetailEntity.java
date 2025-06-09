package aba3.lucid.domain.calendar.entity;

import aba3.lucid.common.enums.Color;
import aba3.lucid.domain.calendar.dto.CalendarDetailRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Entity
@Table(name = "calendar_detail")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarDetailEntity {

    // 캘린더 상세 id
    @Id
    @Column(name = "cal_dt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calDtId;

    // ManyToOne
    // 캘린더 id(날짜 식별하기 위해서)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cal_id")
    @Setter
    private CalendarEntity calendar;

    // 제목
    @Column(name = "cal_dt_title", columnDefinition = "CHAR(100)", nullable = false)
    private String calDtTitle;

    // 내용
    @Column(name = "cal_dt_content", columnDefinition = "CHAR(255)", nullable = false)
    private String calDtContent;

    // 시작 시간
    @Column(name = "cal_dt_start", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime calDtStart;

    // 종료 시간
    @Column(name = "cal_dt_end", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime calDtEnd;

    // 캘린더 색상 RGB 값
    @Enumerated(EnumType.STRING)
    @Column(name = "cal_dt_color", columnDefinition = "CHAR(6)", nullable = false)
    private Color calDtColor = Color.BLUE;

    // 담당자
    @Column(name = "cal_dt_manager", columnDefinition = "CHAR(20)")
    private String calDtManager;

    // 메모
    @Column(name = "cal_dt_memo", columnDefinition = "CHAR(255)")
    private String calDtMemo;

    public void updateField(CalendarDetailRequest request) {
        this.calDtColor = request.getColor();
        this.calDtMemo = request.getMemo();
        this.calDtStart = request.getStartTime();
        this.calDtEnd = request.getEndTime();
        this.calDtTitle = request.getTitle();
        this.calDtContent = request.getContent();
        this.calDtManager = request.getManager();
    }


//    @PrePersist
//    protected void onCreate() {
//        if (this.calDtColor == null) {
//            this.calDtColor = "845EC2"; // 기본값 설정
//        }
//    }
}
