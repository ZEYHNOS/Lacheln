package aba3.lucid.domain.cart.entity;


import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "cart")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartId; //장바구니ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity users; //소비자ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product; //상품ID

    @Column(name = "cart_date", nullable = false)
    private LocalDateTime cartDate; //일정 날짜

    @Column(name = "cart_quantity")
    private int cartQuantity; //구매 수량
}
