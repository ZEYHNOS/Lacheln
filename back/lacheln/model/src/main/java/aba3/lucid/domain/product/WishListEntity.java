package aba3.lucid.domain.product;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pdId;

    private String userId;

}
