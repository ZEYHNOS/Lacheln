package aba3.lucid.domain.product;

import aba3.lucid.domain.product.enums.DressSize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "dress_size")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DressSizeEntity {

    @Id
    @Column(name = "dress_size_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dressSizeId;

    @ManyToOne
    private DressEntity dress;

    @Column(name = "size", nullable = false)
    @Enumerated(EnumType.STRING)
    private DressSize size;

    @Column(name = "dress_size_stock", nullable = false)
    private int dressSizeStock;

}
