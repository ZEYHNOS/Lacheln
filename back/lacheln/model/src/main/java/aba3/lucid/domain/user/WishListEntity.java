package aba3.lucid.domain.user;

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

    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
    private String userId; //소비자ID

    @Column(name = "pd_id")
    private long pdId; //상품ID
}
