package aba3.lucid.calendarDetail.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class CalendarDetailId implements Serializable {
    private Long calDtId;
    private LocalDate calId;
    private Long cpId;
}
