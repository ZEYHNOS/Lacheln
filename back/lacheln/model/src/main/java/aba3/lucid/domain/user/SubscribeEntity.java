package aba3.lucid.domain.user;

import aba3.lucid.domain.company.CompanyEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name = "subscribe")
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeEntity {

    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscribe_id")
    private long subscribeId;

    // 외래키(업체ID)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity company;

    // 외래키2(소비자ID)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity user;
}
