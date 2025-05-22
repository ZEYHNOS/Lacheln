package aba3.lucid.domain.cart.entity;

import aba3.lucid.domain.product.entity.OptionDetailEntity;
import aba3.lucid.domain.product.entity.OptionEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

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
    private Long cartDtId;

    @ManyToOne(fetch = FetchType.LAZY)
    private CartEntity cart; //장바구니ID

    @Column(name = "op_id", nullable = false)
    private Long optionId; //옵션ID

    @Column(name = "op_name", nullable = false)
    private String optionName; // 옵션 이름

    @Column(name = "op_dt_id", nullable=false)
    private Long optionDetailId; //옵션상세ID

    @Column(name = "op_dt_name", nullable = false)
    private String optionDetailName; // 옵션 상세 이름

    @Column(name = "cart_dt_quantity")
    private int cartDtQuantity; // 상세 옵션 갯수

    @Column(name = "op_price", nullable = false)
    private BigInteger optionPrice; // 해당 옵션 가격

    @Column(name = "op_tasktime", nullable = false)
    private int optionTaskTime; // 옵션 작업 추가 시간
}
