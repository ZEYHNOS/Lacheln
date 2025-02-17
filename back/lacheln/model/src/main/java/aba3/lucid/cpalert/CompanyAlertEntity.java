package aba3.lucid.cpalert;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "company_alert")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyAlertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpAlertId;

    private long cpId;

    private String alertTitle;

    private String alertContent;

    private LocalDateTime sendTime;

    private boolean alertRead;

    private String directUrl;

}
