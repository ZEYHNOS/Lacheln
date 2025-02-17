package aba3.lucid.report;

import aba3.lucid.report.enums.ReportCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "report")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reportId;

    private String userId;

    private long cpId;

    private String reportTitle;

    private String reportContent;

    private LocalDateTime createAt;

    private String reportTarget;

    @Enumerated(EnumType.STRING)
    private ReportCategory reportCategory;

}
