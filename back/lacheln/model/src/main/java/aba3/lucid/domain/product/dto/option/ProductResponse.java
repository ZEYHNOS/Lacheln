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
    private Long productId;

    // 상품 이름
    private String productName;

    // 업체 id
    private Long companyId;

    // 업체 이름
    private String companyName;

    // 가격
    private BigInteger price;

    // 대표 이미지
    private String imageUrl;

    // 상태
    private ProductStatus status;

}
