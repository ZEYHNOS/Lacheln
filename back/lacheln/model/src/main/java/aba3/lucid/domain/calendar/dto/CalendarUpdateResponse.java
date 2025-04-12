package aba3.lucid.domain.calendar.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CalendarUpdateResponse {
    private  long calId;
    private LocalDate calDate;
    private  long companyId;
    private List<CalendarDetailResponse> details;
}
