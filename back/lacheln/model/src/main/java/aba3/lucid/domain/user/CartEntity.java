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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pdId;

    private String userId;

}
