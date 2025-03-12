package aba3.lucid.domain.company.entity;

import aba3.lucid.domain.company.enums.SNS;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "social")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialEntity {

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long snsId;

    // foreign key
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company")
    private CompanyEntity company;

    // SNS 이름 및 이미지 주소
    @Enumerated(EnumType.STRING)
    @Column(name = "sns_name", columnDefinition = "CHAR(30)", nullable = false)
    private SNS snsName;

    // 업체 다이렉트 사이트 주소
    @Column(name = "sns_url", columnDefinition = "CHAR(255)", nullable = false)
    private String snsUrl;
}
