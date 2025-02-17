package aba3.lucid.List;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Table(name="wishlist")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pdId;

    private String userId;

}
