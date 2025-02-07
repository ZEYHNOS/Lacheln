package aba3.lucid.calendarDetail.model;

import aba3.lucid.calander.model.CalendarEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity(name = "calendarDetail")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(CalendarDetailId.class)
public class CalendarDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calDtId")
    private Long calDtId;

    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "calId", referencedColumnName = "cpId"),
            @JoinColumn(name = "cpId", referencedColumnName = "calId")})
    private CalendarEntity calendar;

    private String calDtTitle;

    private String calDtContent;

    private LocalDateTime calDtStart;

    private LocalDateTime calDtEnd;

    private String calDtColor;

    private String calDtManager;

    private String calDtMemo;
}
