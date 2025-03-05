package aba3.lucid.domain.product;

import aba3.lucid.common.enums.BinaryChoice;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "studio")
public class StudioEntity {
    // 스튜디오 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stdId;

    // 상품(외래키)
    @OneToOne
    @JoinColumn(name = "pd_id")
    private ProductEntity product;

    // 실내촬영여부
    @Column(name = "std_in_available", nullable = false)
    private BinaryChoice stdInAvailable;

    // 야외촬영여부
    @Column(name = "std_out_available", nullable = false)
    private BinaryChoice stdOutAvailable;

    // 최대수용인원
    @Column(name = "std_max_people", nullable = false)
    private int stdMaxPeople;

    // 배경선택여부
    @Column(name = "std_bg_options", nullable = false)
    private BinaryChoice stdBgOptions;
}
