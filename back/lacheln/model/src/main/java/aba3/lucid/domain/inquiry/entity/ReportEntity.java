package aba3.lucid.domain.inquiry.entity;

import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.inquiry.enums.ReportCategory;
import aba3.lucid.domain.inquiry.enums.ReportStatus;
import aba3.lucid.domain.user.entity.UsersEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.List;

import java.time.LocalDateTime;

@Setter
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
    @Column(name = "report_id")
    private long reportId;

    @Column(name = "report_title", length = 100, nullable = false)
    private String reportTitle;

    @Column(name = "report_content", columnDefinition = "TEXT", nullable = false)
    private String reportContent;

    @Column(name = "report_created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime reportCreatedAt;

    @Column(name = "report_target", columnDefinition = "char(1)", nullable = false)
    private String reportTarget;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_category", columnDefinition = "char(50)", nullable = false)
    private ReportCategory reportCategory;

    @JsonIgnore
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<ReportImageEntity> reportImages;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_status", columnDefinition = "char(20)", nullable = false)
    @Builder.Default
    private ReportStatus reportStatus = ReportStatus.NEW;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @Column(name = "target_name", columnDefinition = "char(50)")
    private String targetName;

    @Column(name = "reporter_name", columnDefinition = "char(50)")
    private String reporterName;
}
