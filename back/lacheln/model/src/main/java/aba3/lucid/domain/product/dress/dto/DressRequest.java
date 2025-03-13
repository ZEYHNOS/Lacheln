package aba3.lucid.domain.product.dress.dto;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.enums.Color;
import aba3.lucid.domain.product.enums.DressSize;
import aba3.lucid.domain.product.dto.ProductRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class DressRequest extends ProductRequest {

    // 실내 여부
    @NotNull
    private BinaryChoice inAvailable;

    // 야외 여부
    @NotNull
    private BinaryChoice outAvailable;

    // 드레스 색상
    @NotNull
    private Color color;

    // 해당 드레스 상품 사이즈 리스트
    @Valid
    private List<DressSizeDto> sizeList;

    @AssertTrue(message = "드레스 사이즈가 중복되거나 사이즈가 존재하지 않습니다.")
    public boolean dressSizeValidator() {
        // 사이즈가 없을 때 false
        if (sizeList == null || sizeList.size() == 0) {
            return false;
        }

        // 사이즈가 중복이 있다면 false
        Set<DressSize> set = new HashSet<>();
        for (DressSizeDto size : sizeList) {
            if (!set.add(size.getSize())) {
                return false;
            }
        }

        return true;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class DressSizeDto {

        // 드레스 사이즈
        @NotNull
        private DressSize size;

        // 드레스 사이즈 별 재고
        @Min(1)
        private int stock;
    }
}
