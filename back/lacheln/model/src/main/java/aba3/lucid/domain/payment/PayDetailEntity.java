package aba3.lucid.domain.payment;

import aba3.lucid.domain.product.OptionDetailEntity;
import aba3.lucid.domain.product.OptionEntity;
import aba3.lucid.domain.product.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "pay_detail")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long payDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    private PayManagementEntity payManagement;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    private OptionEntity option;

    @ManyToOne(fetch = FetchType.LAZY)
    private OptionDetailEntity optionDetail;

    @Column(name = "pay_dt_quantity", columnDefinition = "INT")
    private int payDtQuantity;
}
