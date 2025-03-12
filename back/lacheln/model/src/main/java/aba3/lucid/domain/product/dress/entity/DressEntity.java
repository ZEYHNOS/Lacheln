package aba3.lucid.domain.product.dress.entity;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.enums.Color;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        this.product.changeDescription(dressRequest.getDescription()); // 상세 설명 변경
        this.product.changeProductName(dressRequest.getName()); // 상품 이름 변경
        this.product.changePrice(dressRequest.getPrice()); // 가격 변경
        this.product.changeRec(dressRequest.getRec()); // 추천 상품 변경
        this.product.changeTaskTime(dressRequest.getTaskTime()); // 작업 시간 변경
        this.product.changeStatus(dressRequest.getStatus()); // 상태 변경
        this.product.changeHashTag(dressRequest.getHashTagList()); // 해시 태그 변경

        // TODO 옵션 변경하기

        // TODO 옵션 상세 변경하기
    }
}
