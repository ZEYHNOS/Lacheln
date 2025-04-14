package aba3.lucid.domain.schedule.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemporaryHolidayResponse {
    private long thId;
    private long cpId;
    private LocalDateTime thDate;
    private String thReason;
}
