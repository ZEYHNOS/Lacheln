package aba3.lucid.domain.company;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpAlertId;

    // TODO ManyToOne
    private long cpId;

    @Column(name = "cp_alert_title", length = 50, nullable = false)
    private String cpAlertTitle;

    @Column(name = "cp_alert_content", length = 100, nullable = false)
    private String cpAlertContent;

    @Column(name = "cp_alert_sendtime", nullable = false, updatable = false)
    private LocalDateTime cpAlertSendTime;

    @Column(name = "cp_alert_read", columnDefinition = "CHAR(1)", nullable = false)
    private String cpAlertRead;

    @Column(name = "cp_alert_access_url", columnDefinition = "VARCHAR(255)")
    private String cpAlertAccessUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity company;

    // JPA 엔티티가 데이터베이스에 저장되기 전 자동으로 실행
    // 데이터베이스에 INSERT 되기 직전 실행
//    @PrePersist
//    protected void onCreate() {
//        // JPA 내부적으로 호출하는 메소드이므로 public 열 필요 없음 private 제어시 JPA 내부에서 접근 불가능해서 에러가 발생할 수 있음
//        this.cpAlertSendTime = LocalDateTime.now();
//        this.cpAlertRead = "0";
//    }

}
