package aba3.lucid.domain.product.entity;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@Table(name = "product")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "category")
public abstract class ProductEntity {

    // 상품 id(기본키)
    @Id
    @Column(name = "pd_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pdId;

    // 업체 ID
    @JoinColumn(name = "cp_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CompanyEntity company;

    // 상품명
    @Column(name = "pd_name", nullable = false, columnDefinition = "VARCHAR(100)")
    private String pdName;

    // 상품 가격
    @Column(name = "pd_price", columnDefinition = "BIGINT", nullable = false)
    private BigInteger pdPrice;

    // 상품 상태
    @Column(name = "pd_status", nullable = false, columnDefinition = "CHAR(20)")
    @Enumerated(EnumType.STRING)
    private ProductStatus pdStatus;

    // 상품 추천 여부
    @Column(name = "pr_rec", nullable = false, columnDefinition = "CHAR(1)")
    @Enumerated(EnumType.STRING)
    private BinaryChoice pdRec;

    // 상품 소요 시간
    @Column(name = "pd_tasktime", nullable = false)
    private LocalTime pdTaskTime;

    // 상품 설명(블로그처럼 이미지, 동영상을 업체가 자유롭게 저장)
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDescriptionEntity> productDescriptionEntityList;

    // 옵션 리스트
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionEntity> opList;


    // 상품 태그 리스트
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HashtagEntity> hashtagList;


    // 상품 이미지 리스트
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImageEntity> imageList;

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;


    // 상품 이미지 리스트 업데이트
    public void updateProductImage(List<ProductImageEntity> productImageEntityList) {
        if (productImageEntityList.isEmpty()) {
            // TODO 생각해보자
            return;
        }

        if (this.imageList == null) {
            this.imageList = new ArrayList<>();
        }

        this.imageList.addAll(productImageEntityList);
    }

    // TODO 상품 이름 길이 제약조건 이야기 하기
    public void updateProductName(String name) {
        if (name == null || (name.length() < 3 && name.length() > 100)) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "상품 이름 값이 없거나 조건에 맞지 않습니다.");
        }

        this.pdName = name;
    }

    // 상품 삭제 복구하기
    // TODO 권한 부여하기
    public void recoverProduct(String role) {
        if (!role.equals("ADMIN")) {
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }

        this.pdStatus = ProductStatus.INACTIVE;
    }


    // 상태 변경
    public void updateStatus(ProductStatus changeStatus) {
        if (changeStatus == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "상태 값이 존재하지 않습니다.");
        }

        // 변경하지 않을 때 return
        if (this.getPdStatus().equals(changeStatus)) {
            return;
        }

        // 패키지 상품 일 때 조작 불가능
        if (this.pdStatus.equals(ProductStatus.PACKAGE)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        this.pdStatus = changeStatus;
    }

    // 패키지에서 해당 상품을 삭제 시킬 때
    public void updateStatusToPackageDelete() {
        if (!this.pdStatus.equals(ProductStatus.PACKAGE)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        this.pdStatus = ProductStatus.INACTIVE;
    }

    // 상품을 패키지에 등록
    public void updateStatusToPackage() {
        // 삭제 상태이거나 패키지 상태일 때(똑같은 상품이 2개의 패키지에 들어갔을 때 유효기간에 따라 관리가 어려움)
        if (this.pdStatus == ProductStatus.REMOVE || this.pdStatus == ProductStatus.PACKAGE) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        // 공개 상태일 때
        if (this.pdStatus == ProductStatus.ACTIVE) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "비공개 상태로 변경해주세요");
        }

        this.pdStatus = ProductStatus.PACKAGE;
    }


    // 업체 추천 변경
    public void updateRec(BinaryChoice rec) {
        if (rec == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "업체 추천 값이 존재하지 않습니다.");
        }

        this.pdRec = rec;
    }

    public void updateDescription(List<ProductDescriptionEntity> descriptionEntityList) {
        if (this.productDescriptionEntityList == null) {
            this.productDescriptionEntityList = new ArrayList<>();
        }

        this.productDescriptionEntityList.clear();
        if (descriptionEntityList == null || descriptionEntityList.isEmpty()) {
            return;
        }

        this.productDescriptionEntityList.addAll(descriptionEntityList);;
    }

    // 소요 시간 변경
    public void updateTaskTime(LocalTime time) {
        if (time == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "소요 시간 값이 0 혹은 음수 입니다.");
        }

        this.pdTaskTime = time;
    }

    public void updateOptionList(List<OptionEntity> optionEntityList) {
        if (this.opList == null) {
            this.opList = new ArrayList<>();
        }

        this.opList.clear();
        if (optionEntityList == null) {
            return;
        }

        this.opList.addAll(optionEntityList);
    }

    // 태그 리스트 삭제 후 새로 넣기
    public void updateHashTag(List<HashtagEntity> hashtagList) {
        if (this.hashtagList == null) {
            this.hashtagList = new ArrayList<>();
        }

        this.hashtagList.clear();
        if (hashtagList == null) {
            return;
        }

        // 중복 제거
        Set<String> hashTagSet = new HashSet<>();
        List<HashtagEntity> uniqueList = new ArrayList<>();
        for (HashtagEntity entity : hashtagList) {
            if (hashTagSet.add(entity.getTagName())) {
                uniqueList.add(entity);
            }
        }

        this.hashtagList.addAll(uniqueList);
    }

    public void updateFormList(List<OptionEntity> opList, List<HashtagEntity> hashtagEntityList, List<ProductImageEntity> productImageEntityList, List<ProductDescriptionEntity> descriptionEntityList) {
        updateOptionList(opList);
        updateProductImage(productImageEntityList);
        updateHashTag(hashtagEntityList);
        updateDescription(descriptionEntityList);
    }

    public void deleteProduct(LocalDateTime now) {
        if (now == null) {
            throw new ApiException(ErrorCode.NULL_POINT);
        }

        this.deleteDate = now;
    }
}
