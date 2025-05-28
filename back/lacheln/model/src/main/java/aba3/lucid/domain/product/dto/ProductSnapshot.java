package aba3.lucid.domain.product.dto;

import aba3.lucid.domain.cart.entity.CartEntity;
import aba3.lucid.domain.product.dto.option.OptionSnapshot;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSnapshot {

    private Long id;
    private String name;
    private BigInteger price;
    private LocalTime taskTime;
    private List<OptionSnapshot> optionSnapshotList;

    public ProductSnapshot(CartEntity cart) {
        this.id = cart.getPdId();
        this.name = cart.getPdName();
        this.taskTime = cart.getTaskTime();
        this.price = cart.getPrice();
        this.optionSnapshotList = cart.getCartDetails().stream()
                .map(it -> OptionSnapshot.builder()
                        .optionId(it.getOptionId())
                        .optionName(it.getOptionName())
                        .optionTaskTime(it.getOptionTaskTime())
                        .optionDetailId(it.getOptionDetailId())
                        .optionDetailName(it.getOptionDetailName())
                        .build())
                .toList()
                ;
    }
}