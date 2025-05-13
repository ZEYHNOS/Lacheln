    package aba3.lucid.domain.cart.entity;


    import aba3.lucid.domain.product.entity.ProductEntity;
    import aba3.lucid.domain.user.entity.UsersEntity;
    import jakarta.persistence.*;
    import lombok.*;

    import java.math.BigInteger;
    import java.time.LocalDateTime;
    import java.util.List;

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
        private long cartId; // 장바구니 ID

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private UsersEntity users; // 소비자 ID

        @Column(name = "pd_id", nullable = false)
        private Long productId; // 상품 ID

        @Column(name = "cp_id", nullable = false)
        private Long cpId; // 업체 ID

        @Column(name = "pd_name", nullable = false)
        private String productName; // 상품 이름

        @Column(name = "cart_date", nullable = false)
        private LocalDateTime cartDate; //일정 날짜

        @Column(name = "cart_quantity")
        private int cartQuantity; //구매 수량

        @Column(name = "pd_price", nullable = false)
        private BigInteger price; // 가격

        @Column(name = "task_time", nullable = false)
        private int taskTime; // 작업 시간

        @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
        private List<CartDetailEntity> cartDetails;

        public void addCartDetail(List<CartDetailEntity> cartDetailEntity) {
            this.cartDetails = cartDetailEntity;
        }
    }
