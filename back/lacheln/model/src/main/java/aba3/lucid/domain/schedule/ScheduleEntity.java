package aba3.lucid.domain.schedule;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="schedule")
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long scheduleId;

    private String userId;

    private String companyId;

    private String productId;

    private String scheduleStatus;//일정 상태:예정, 중, 완료, 취소

    private String scheduleDate;//일정 날짜

    private String scheduleDetails;//특이사항

    private String schedulePerson;//인원

}
