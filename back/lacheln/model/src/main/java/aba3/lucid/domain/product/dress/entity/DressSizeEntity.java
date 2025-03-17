package aba3.lucid.domain.product.dress.entity;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
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
    private long dressSizeId; //드레스 사이즈 ID

    @JoinColumn(name = "pd_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DressEntity dress; //드레스 ID

    @Column(name = "size", nullable = false)
    @Enumerated(EnumType.STRING)
    private DressSize size; //사이즈

    @Column(name = "dress_size_stock")
    private int dressSizeStock; //재고

    public void changeStock(int stock) {
        if (stock <= 0) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "재고가 1개 이상 존재해야 합니다.");
        }

        this.dressSizeStock = stock;
    }

}
