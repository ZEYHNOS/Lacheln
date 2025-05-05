package aba3.lucid.domain.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleCreateRequest {

    private Long companyId;

    private String userId;

    private String userName;

    private LocalDateTime scheduleDate;

    private String notify;

    private int person;

    private int totalTime;

}
