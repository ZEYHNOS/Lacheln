package aba3.lucid.domain.inquiry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "report_image")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportImageEntity {

    @Id
    @Column(name = "report_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reportImageId;

    @Column(name = "report_image_url", columnDefinition = "CHAR(255)", nullable = false)
    private String reportImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private ReportEntity report;
}
