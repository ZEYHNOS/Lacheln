package aba3.lucid.domain.user;

import aba3.lucid.domain.product.ProductEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Table(name="wishlist")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishListEntity {

    @Id
    @Column(name = "wishlist_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long wishlistId; //찜ID

    @ManyToOne(fetch = FetchType.LAZY)
    private UsersEntity users;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;
}
