package aba3.lucid.reportimages;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private long reportImageId;

    // TODO ManyToOne
    private long reportId;

    @Column(name = "report_image_url", columnDefinition = "CHAR(255)", nullable = false)
    private String reportImageUrl;
}
