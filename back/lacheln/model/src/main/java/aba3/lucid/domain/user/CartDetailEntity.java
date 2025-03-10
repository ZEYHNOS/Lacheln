package aba3.lucid.domain.user;

import aba3.lucid.domain.product.OptionDetailEntity;
import aba3.lucid.domain.product.OptionEntity;
import aba3.lucid.domain.product.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cart_detail")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailEntity {

    /**
     * 장바구니 상세(옵션) 정보 저장
     *
     * Fetch.LAZY 하지 않음
     * 이유 : 장바구니를 조회할 때 무조건 상세 정보까지 필요함
     */

    // 장바구니 상세 Id
    @Id
    @Column(name = "cart_dt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartDtId;

    @ManyToOne(fetch = FetchType.LAZY)
    private CartEntity cart; //장바구니ID

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product; //상품ID

    @ManyToOne(fetch = FetchType.LAZY)
    private OptionEntity option; //옵션ID

    @ManyToOne(fetch = FetchType.LAZY)
    private OptionDetailEntity optionDetail; //옵션상세ID

    @Column(name = "cart_dt_quantity")
    private int cartDtQuantity; // 상세 옵션 갯수

}
