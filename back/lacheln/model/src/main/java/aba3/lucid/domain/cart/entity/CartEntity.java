package aba3.lucid.domain.cart.entity;


import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
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

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "cart_date", nullable = false)
    private LocalDateTime cartDate; //일정 날짜

    @Column(name = "cart_quantity")
    private int cartQuantity; //구매 수량

    @Column(name = "price", nullable = false)
    private BigInteger price;

    @Column(name = "task_time", nullable = false)
    private int taskTime;
}
