package aba3.lucid.domain.alert.entity;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.alert.enums.AlertType;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.review.entity.ReviewCommentEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "cp_alert")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyAlertEntity {

    @Id
    @Column(name = "cp_alert_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpAlertId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity company;

    @Column(name = "cp_alert_type", columnDefinition = "VARCHAR(50)", nullable = false)
    private String cpAlertType;

    @Column(name = "cp_alert_text", columnDefinition = "VARCHAR(100)", nullable = false)
    private String cpAlertText;

    @Column(name = "cp_alert_sendtime", nullable = false, updatable = false)
    private LocalDateTime cpAlertSendTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "cp_alert_read", columnDefinition = "CHAR(1)", nullable = false)
    private BinaryChoice cpAlertRead;

    @Column(name = "cp_alert_access_url", columnDefinition = "VARCHAR(255)")
    private String cpAlertAccessUrl;

    public static CompanyAlertEntity createReviewAlert(CompanyEntity company, String userName) {
        return CompanyAlertEntity.builder()
                .company(company)
                .cpAlertType(AlertType.REVIEW.getType())
                .cpAlertText(String.format(AlertType.REVIEW.getText(), userName))
                .cpAlertSendTime(LocalDateTime.now())
                .cpAlertRead(BinaryChoice.N)
                .cpAlertAccessUrl(AlertType.REVIEW.getBaseUrl())
                .build()
                ;
    }

    public void readAlert() {
        this.cpAlertRead = BinaryChoice.Y;
    }

}
