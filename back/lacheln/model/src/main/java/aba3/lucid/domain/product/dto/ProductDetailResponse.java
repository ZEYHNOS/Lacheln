package aba3.lucid.domain.product.dto;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.product.dto.option.OptionDto;
import aba3.lucid.domain.product.enums.ProductStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class ProductDetailResponse {

    // 상품 아이디(웹에서 정렬을 위해 사용
    private Long id;

    // 상품 이름
    private String name;

    // 상품 가격
    private BigInteger price;

    // 상품 상태
    private ProductStatus status;

    // 추천 상태
    private BinaryChoice rec;

    // 소요 시간
    private LocalTime taskTime;

    // 설명
    private List<DescriptionResponse> descriptionList;

    // 태그 리스트
    private List<String> hashTagList;

    // 옵션 리스트
    private List<OptionDto> optionList;

    // 상품 이미지 리스트
    private List<ProductImageResponse> productImageUrl;
}
