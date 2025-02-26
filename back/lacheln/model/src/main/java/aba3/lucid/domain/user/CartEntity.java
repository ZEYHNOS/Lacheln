package aba3.lucid.domain.user;


import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
    private String userId; //소비자ID

    @Column(name = "pd_id", nullable = false)
    private long pdId; //상품ID
}
