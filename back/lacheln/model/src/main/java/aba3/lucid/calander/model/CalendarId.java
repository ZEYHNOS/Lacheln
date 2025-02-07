package aba3.lucid.calander.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CalendarId implements Serializable {
    private LocalDate calId;
    private Long cpId;
}
