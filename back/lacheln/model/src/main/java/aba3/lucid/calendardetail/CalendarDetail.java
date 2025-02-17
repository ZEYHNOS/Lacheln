package aba3.lucid.calendardetail;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Entity
@Table(name = "calendar_detail")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarDetail {

    // 캘린더 상세 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long calDtId;

    // 캘린더 id(날짜 식별하기 위해서)
    private long calId;

    // 제목
    private String calDtTitle;

    // 내용
    private String calDtContent;

    // 시작 시간
    private LocalTime calDtStart;

    // 종료 시간
    private LocalTime calDtEnd;

    // 캘린더 색상
    // todo enum 처리
    private String calDtColor;

    // 담당자
    private String calDtManager;

    // 메모
    private String memo;

}
