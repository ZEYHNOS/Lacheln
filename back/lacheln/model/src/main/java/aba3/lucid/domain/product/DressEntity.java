package aba3.lucid.domain.product;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.enums.Color;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.dto.product.dress.DressRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "dress")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DressEntity {

    @Id
    @Column(name = "dress_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dressId; //드레스 ID

    @OneToOne(fetch = FetchType.LAZY)
    private ProductEntity product; //상품

    // 실내촬영여부
    @Column(name = "dress_in_available", nullable = false)
    @Enumerated(EnumType.STRING)
    private BinaryChoice dressInAvailable; //실내촬영여부

    // 야외촬영여부
    @Column(name = "dress_out_available", nullable = false)
    @Enumerated(EnumType.STRING)
    private BinaryChoice dressOutAvailable; //야외촬영여부

    // 드레스 색상
    @Column(name = "dress_color", nullable = false)
    @Enumerated(EnumType.STRING)
    private Color dressColor; //색상

    @JsonIgnore
    @OneToMany(mappedBy = "dress")
    private List<DressSizeEntity> dressSizeList;

    // 내부 촬영 변경
    public void changeInAvailable(BinaryChoice binaryChoice) {
        if (binaryChoice == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "내부촬영여부 값이 존재하지 않습니다.");
        }

        this.dressInAvailable = binaryChoice;
    }

    // 외부 촬영 변경
    public void changeOutAvailable(BinaryChoice binaryChoice) {
        if (binaryChoice == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "외부촬영여부 값이 존재하지 않습니다.");
        }

        this.dressOutAvailable = binaryChoice;
    }

    // 드레스 색 변경
    public void changeColor(Color color) {
        if (color == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "color 값이 존재하지 않습니다.");
        }

        this.dressColor = color;
    }


    public void changeProductInfo(DressRequest dressRequest) {
        this.product.changeDescription(dressRequest.getDescription());
        this.product.changeProductName(dressRequest.getName());
        this.product.changePrice(dressRequest.getPrice());
        this.product.changeRec(dressRequest.getRec());
        this.product.changeTaskTime(dressRequest.getTaskTime());
        this.product.changeStatus(dressRequest.getStatus());

        List<HashtagEntity> hashtagEntityList = new ArrayList<>();
        for (String hasTagName : dressRequest.getHashTagList()) {
            hashtagEntityList.add(HashtagEntity.builder()
                    .product(this.product)
                    .tagName(hasTagName)
                    .build());
        }

        this.product.updateHashTagList(hashtagEntityList);
    }

}
