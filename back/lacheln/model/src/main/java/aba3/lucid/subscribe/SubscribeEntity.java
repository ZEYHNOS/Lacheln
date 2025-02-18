package aba3.lucid.subscribe;

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
    // TODO ManyToOne 설정
    @Column(nullable = false)
    private long cpId;

    // 외래키2(소비자ID)
    // TODO ManyToOne 설정
    @Column(nullable = false)
    private String userId;
}
