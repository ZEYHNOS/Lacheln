package aba3.lucid.domain.schedule.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemporaryHolidayResponse {
    private long id;
    private long cpId;
    private LocalDate date;
    private String reason;
}
