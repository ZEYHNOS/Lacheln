package aba3.lucid.domain.product.dto.option;

import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.enums.ProductStatus;
import lombok.*;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductResponse {

    // 정렬을 위한 id
    private long id;

    // 상품 이름
    private String name;

    // 가격
    private BigInteger price;

    // 대표 이미지
    private String imageUrl;

}
