    package aba3.lucid.domain.cart.entity;

    import aba3.lucid.common.exception.ApiException;
    import aba3.lucid.common.status_code.ErrorCode;
    import aba3.lucid.domain.cart.dto.CartRequest;
    import aba3.lucid.domain.cart.dto.CartUpdateRequest;
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

        @Column(name = "pd_id", nullable = false)
        private Long pdId; // 상품 ID

        @Column(name = "cp_id", nullable = false)
        private Long cpId; // 업체 ID

        @Column(name = "pack_id")
        private Long packId; // 패키지 ID(Null가능)

        @Column(name = "pd_image_url", nullable = false)
        private String pdImageUrl;

        @Column(name = "pd_name", nullable = false)
        private String pdName; // 상품 이름

        @Column(name = "start_datetime", nullable = false)
        private LocalDateTime startDatetime; //일정 날짜

        @Column(name = "cart_quantity")
        private Integer cartQuantity; //구매 수량

        @Column(name = "pd_price", nullable = false)
        private BigInteger price; // 가격

        @Column(name = "task_time", nullable = false)
        private LocalTime taskTime; // 작업 시간

        @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
        private List<CartDetailEntity> cartDetails;

        public void addCartDetail(List<CartDetailEntity> cartDetailEntity) {
            if(cartDetailEntity == null) {
                throw new ApiException(ErrorCode.GONE, "요청된 데이터가 없습니다.");
            }
            this.cartDetails = cartDetailEntity;
        }

        public void updateStartDateTime(LocalDateTime startDateTime) {
            if(startDateTime == null)    {
                throw new ApiException(ErrorCode.GONE ,"요청된 데이터가 없습니다.");
            }
            this.startDatetime = startDateTime;
        }

        public void updateCartQuantity(Integer cartQuantity) {
            if(cartQuantity == null)    {
                throw new ApiException(ErrorCode.GONE ,"요청된 데이터가 없습니다.");
            }
            this.cartQuantity = cartQuantity;
        }

        public void updatePrice(BigInteger price) {
            if(price == null)    {
                throw new ApiException(ErrorCode.GONE ,"요청된 데이터가 없습니다.");
            }
            this.price = price;
        }

        public void updateTaskTime(LocalTime taskTime) {
            if(taskTime == null)    {
                throw new ApiException(ErrorCode.GONE ,"요청된 데이터가 없습니다.");
            }
            this.taskTime = taskTime;
        }

        public void updatePackId(Long packId) {
            if(packId == null)    {
                throw new ApiException(ErrorCode.GONE, "요청된 데이터가 없습니다.");
            }
            this.packId = packId;
        }

        public void updateCart(CartUpdateRequest request)    {
            CartRequest cart = request.getCartRequest();
            updateCartQuantity(cart.getCartQuantity());
            updatePrice(cart.getPdPrice());
            updatePackId(cart.getPdId());
            updateStartDateTime(cart.getStartDateTime());
            updateTaskTime(cart.getPdTaskTime());
        }
    }
