package aba3.lucid.domain.inquiry;

import aba3.lucid.domain.company.CompanyEntity;
import aba3.lucid.domain.inquiry.enums.ReportCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.List;

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
    @Column(name = "report_id")
    private long reportId;

    @Column(name = "user_id", columnDefinition = "char(36)", nullable = false)
    private String userId; // 추후 ManyToOne으로 수정

    @Column(name = "cp_id", nullable = false)
    private long cpId;  // 추후 ManyToOne으로 수정

    @Column(name = "report_title", length = 100, nullable = false)
    private String reportTitle;

    @Column(name = "report_content", columnDefinition = "TEXT", nullable = false)
    private String reportContent;

    @Column(name = "report_created_at", nullable = false, updatable = false)
    private LocalDateTime reportCreatedAt;

    @Column(name = "report_target", columnDefinition = "char(1)", nullable = false)
    private String reportTarget;


    @Enumerated(EnumType.STRING)
    @Column(name = "report_category", columnDefinition = "char(50)", nullable = false)
    private ReportCategory reportCategory;

    @OneToMany( mappedBy = "report", cascade = CascadeType.ALL)
    private List<ReportImageEntity> reportImageId;     //??????????

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity company;

}
