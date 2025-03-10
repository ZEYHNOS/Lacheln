package aba3.lucid.dto.product;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.product.enums.ProductStatus;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductResponse {

    // 상품 이름
    private String name;

    // 상품 가격
    private BigInteger price;

    // 상품 상태
    private ProductStatus status;

    // 추천 상태
    private BinaryChoice rec;

    // 소요 시간
    private int taskTime;

    // 설명
    private String description;

    private List<String> hashTagList;

}
