package aba3.lucid.domain.user;


import aba3.lucid.domain.product.ProductEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private UsersEntity users;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;
}
