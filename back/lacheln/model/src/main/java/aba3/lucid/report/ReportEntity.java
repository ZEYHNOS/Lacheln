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
    //TOdo
    //Entity 만들기
    //type, column 제대로 만들기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false)
    private long reportId;

    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @Column(name = "cp_id", nullable = false)
    private long cpId;

    @Column(name = "report_title", length = 100, nullable = false)
    private String reportTitle;

    @Column(name = "report_content", columnDefinition = "TEXT", nullable = false)
    private String reportContent;

    @Column(name = "report_created_at", nullable = false, updatable = false)
    private LocalDateTime reportCreatedAt;

    @Column(name = "report_target", length = 1, nullable = false)
    private String reportTarget;


    @Enumerated(EnumType.STRING)
    @Column(name = "report_category", length = 50, nullable = false)
    private ReportCategory reportCategory;

}
