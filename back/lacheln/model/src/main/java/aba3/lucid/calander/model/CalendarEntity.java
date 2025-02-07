package aba3.lucid.calander.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity(name = "calendar")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(CalendarId.class)
public class CalendarEntity {

    @Id @Column(name = "calId")
    private LocalDateTime calId;

    @Id
    @ManyToOne
    @JoinColumn(name = "cpId")
    private CompanyEntity cpId;
}
