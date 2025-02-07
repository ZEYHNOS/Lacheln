package aba3.lucid.productimage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDto {
    // 상품 이미지 ID
    private Long pdImageId;

    // TODO 해당 속성은 외래키임으로 참조 데이터를 dto에 담을 시,
    // TODO 아래 속성을 제거 한 후 참조된 데이터값을 넣기
    private Long pdId;

    // 이미지 url
    private String imageUrl;
}