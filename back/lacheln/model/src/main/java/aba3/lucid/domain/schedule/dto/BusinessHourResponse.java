package aba3.lucid.domain.schedule.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class BusinessHourResponse {

    private LocalTime start;

    private LocalTime end;

}
