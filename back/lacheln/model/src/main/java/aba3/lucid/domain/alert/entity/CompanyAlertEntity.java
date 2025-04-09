package aba3.lucid.domain.alert.entity;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.company.entity.CompanyEntity;
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

    @Column(name = "cp_alert_title", columnDefinition = "VARCHAR(50)", nullable = false)
    private String cpAlertTitle;

    @Column(name = "cp_alert_content", columnDefinition = "VARCHAR(100)", nullable = false)
    private String cpAlertContent;

    @Column(name = "cp_alert_sendtime", nullable = false, updatable = false)
    private LocalDateTime cpAlertSendTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "cp_alert_read", columnDefinition = "CHAR(1)", nullable = false)
    private BinaryChoice cpAlertRead;

    @Column(name = "cp_alert_access_url", columnDefinition = "VARCHAR(255)")
    private String cpAlertAccessUrl;

    public void readAlert() {
        this.cpAlertRead = BinaryChoice.Y;
    }

}
