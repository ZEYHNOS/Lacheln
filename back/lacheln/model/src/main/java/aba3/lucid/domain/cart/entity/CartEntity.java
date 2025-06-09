    package aba3.lucid.domain.cart.entity;

    import aba3.lucid.common.exception.ApiException;
    import aba3.lucid.common.status_code.ErrorCode;
    import aba3.lucid.domain.company.enums.CompanyCategory;
    import aba3.lucid.domain.user.entity.UsersEntity;
    import jakarta.persistence.*;
    import lombok.*;

    import java.math.BigInteger;
    import java.time.LocalDateTime;
    import java.time.LocalTime;
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
        private Long cartId; // 장바구니 ID

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private UsersEntity users; // 소비자 ID

        @Column(name = "cp_id", nullable = false)
        private Long cpId; // 업체 ID

        @Column(name = "pd_id")
        private Long pdId; // 상품 ID

        @Column(name = "pack_id")
        private Long packId; // 패키지 ID(Null가능)

        @Column(name = "pack_name")
        private String packName;

        @Column(name = "pd_image_url", nullable = false)
        private String pdImageUrl;

        @Column(name = "pd_name")
        private String pdName; // 상품, 패키지 이름

        @Column(name = "cp_name", nullable = false)
        private String cpName;

        @Column(name = "start_datetime", nullable = false)
        private LocalDateTime startDatetime; //일정 날짜

        @Column(name = "cart_quantity")
        private Integer cartQuantity; //구매 수량

        @Column(name = "pd_price", nullable = false)
        private BigInteger price; // 가격

        @Column(name = "discount_price")
        private BigInteger discountPrice; // 패키지의 경우 할인된 가격

        @Column(name = "task_time", nullable = false)
        private LocalTime taskTime; // 작업 시간
        
        @Column(name = "manager")
        private String manager; // 메이크업의 담당자 이름

        @Enumerated(EnumType.STRING)
        @Column(name = "category", nullable = false)
        private CompanyCategory category;

        @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
        private List<CartDetailEntity> cartDetails;

        public void addCartDetail(List<CartDetailEntity> cartDetailEntity) {
            if(cartDetailEntity == null) {
                throw new ApiException(ErrorCode.GONE, "요청된 데이터가 없습니다.");
            }
            this.cartDetails = cartDetailEntity;
        }
    }
