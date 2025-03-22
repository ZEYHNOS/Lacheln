package aba3.lucid.domain.product.dto.option;

import aba3.lucid.domain.company.enums.CompanyCategory;
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

    // 업체 명
    private String companyName;

    // 카테고리
    private CompanyCategory companyCategory;

    // 상품 가격
    private String productName;

    // 설명
    private String description;

    // 가격
    private BigInteger price;

    // 대표 이미지
    private String imageUrl;

}
