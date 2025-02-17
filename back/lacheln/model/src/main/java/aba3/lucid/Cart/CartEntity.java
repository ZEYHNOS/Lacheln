package aba3.lucid.Cart;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Table(name = "cart")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String productId;

    private String userId;

}
