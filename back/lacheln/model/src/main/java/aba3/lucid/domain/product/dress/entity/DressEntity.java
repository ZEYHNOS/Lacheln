package aba3.lucid.domain.product.dress.entity;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.enums.Color;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.product.entity.OptionEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.entity.ProductImageEntity;
import aba3.lucid.domain.product.enums.DressSize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@Table(name = "dress")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("dress")
public class DressEntity extends ProductEntity {

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
    @OneToMany(mappedBy = "dress", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DressSizeEntity> dressSizeList;

    // 내부 촬영 변경
    public void updateInAvailable(BinaryChoice binaryChoice) {
        if (binaryChoice == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "내부촬영여부 값이 존재하지 않습니다.");
        }

        this.dressInAvailable = binaryChoice;
    }

    // 외부 촬영 변경
    public void updateOutAvailable(BinaryChoice binaryChoice) {
        if (binaryChoice == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "외부촬영여부 값이 존재하지 않습니다.");
        }

        this.dressOutAvailable = binaryChoice;
    }

    // 드레스 색 변경
    public void updateColor(Color color) {
        if (color == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "color 값이 존재하지 않습니다.");
        }

        this.dressColor = color;
    }


    // 드래스 사이즈 List Update
    public void updateDressSizeList(List<DressSizeEntity> dressSizeList) {
        if (dressSizeList.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER, "드래스 사이즈값이 없습니다.");
        }

        // 드래스 사이즈 중복 여부
        Set<DressSize> dressSizeSet = new HashSet<>();
        for (DressSizeEntity dressSizeEntity : dressSizeList) {
            if (dressSizeSet.contains(dressSizeEntity.getSize())) {
                throw new ApiException(ErrorCode.INVALID_PARAMETER, "중복된 드레스 사이즈가 있습니다.");
            }

            dressSizeSet.add(dressSizeEntity.getSize());
        }

        if (this.dressSizeList != null) {
            this.dressSizeList.clear();
        } else {
            this.dressSizeList = new ArrayList<>();
        }
        this.dressSizeList.addAll(dressSizeList);
    }

    // updateFormRequest
    public void updateFromRequest(DressRequest request, List<OptionEntity> optionEntityList, List<DressSizeEntity> dressSizeList, List<ProductImageEntity> productImageEntityList) {
        updateDescription(request.getDescription());
        updateOutAvailable(request.getOutAvailable());
        updateInAvailable(request.getInAvailable());
        updateProductName(request.getName());
        updateColor(request.getColor());
        updatePrice(request.getPrice());
        updateHashTag(request.getHashTagList());
        updateStatus(request.getStatus());
        updateTaskTime(request.getTaskTime());
        updateRec(request.getRec());
        updateOptionList(optionEntityList);
        updateDressSizeList(dressSizeList);
        updateProductImage(productImageEntityList);
    }
}
