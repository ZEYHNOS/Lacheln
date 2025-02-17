package aba3.lucid.reportimages;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "report_images")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportImagesEntity {

    @Id
    private long reportImageId;

    private long reportId;

    private String reportImageUrl;
}
