package aba3.lucid.List;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Setter
@Getter
@Table(name="wishlist")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String productId;

    private String userId;

}
