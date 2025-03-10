package aba3.lucid.domain.product;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.enums.Color;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@Table(name = "dress")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DressEntity {

    @Id
    @Column(name = "dress_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dressId; //드레스 ID

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product; //상품 ID

    // 실내촬영여부
    @Column(name = "dress_in_available", nullable = false)
    @Enumerated(EnumType.STRING)
    private BinaryChoice dressInAvailable; //실내촬영여부

    // 야외촬영여부
    @Column(name = "dress_out_available", nullable = false)
    @Enumerated(EnumType.STRING)
    private BinaryChoice dressOutAvailable; //야외촬영여부

    // 드레스 색상
    @Column(name = "dress_color", nullable = false)
    @Enumerated(EnumType.STRING)
    private Color dressColor; //색상

    @JsonIgnore
    @OneToMany(mappedBy = "dress")
    private List<DressSizeEntity> dressSizeList;

}
