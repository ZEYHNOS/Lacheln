package aba3.lucid.domain.product.entity;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
    private long pdId;

    // 업체 ID
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
    private int pdTaskTime;

    // 상품 설명(블로그처럼 이미지, 동영상을 업체가 자유롭게 저장)
    @Column(name = "pd_description", columnDefinition = "LONGTEXT")
    private String pdDescription;

    // 옵션 리스트
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<OptionEntity> opList;


    // 상품 태그 리스트
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<HashtagEntity> hashtagList;


    // TODO 상품 이름 길이 제약조건 이야기 하기
    public void updateProductName(String name) {
        if (name == null || (name.length() < 3 && name.length() > 100)) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "상품 이름 값이 없거나 조건에 맞지 않습니다.");
        }

        this.pdName = name;
    }


    // 가격 변경
    public void updatePrice(BigInteger price) {
        if (price.compareTo(BigInteger.ZERO) < 0) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "0 혹은 음수의 값이 들어왔습니다.");
        }

        this.pdPrice = price;
    }

    // 상태 변경
    public void updateStatus(ProductStatus status) {
        if (status == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "상태 값이 존재하지 않습니다.");
        }

        this.pdStatus = status;
    }

    // 해시 태그 변경
    public void updateHashtagList(List<HashtagEntity> list) {
        hashtagList = new ArrayList<>();

        hashtagList.addAll(list);
    }


    // 업체 추천 변경
    public void updateRec(BinaryChoice rec) {
        if (rec == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "업체 추천 값이 존재하지 않습니다.");
        }

        this.pdRec = rec;
    }

    // 소요 시간 변경
    public void updateTaskTime(int time) {
        if (time <= 0) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "소요 시간 값이 0 혹은 음수 입니다.");
        }

        this.pdTaskTime = time;
    }

    // 상세 설명 변경
    public void updateDescription(String description) {
        this.pdDescription = description;
    }

    public void updateOptionList(List<OptionEntity> optionEntityList) {
        if (this.opList != null) {
            this.opList.clear();
        }

        this.opList.addAll(optionEntityList);
    }

    // 태그 리스트 삭제 후 새로 넣기
    public void updateHashTag(List<String> hashTagList) {
        if (hashTagList.size() == 0) {
            return;
        }

        List<HashtagEntity> hashtagEntityList = new ArrayList<>();
        for (String hasTagName : hashTagList) {
            hashtagEntityList.add(HashtagEntity.builder()
                    .product(this)
                    .tagName(hasTagName)
                    .build());
        }

        if (this.hashtagList != null) {
            this.hashtagList.clear();
        }

        this.hashtagList.addAll(hashtagEntityList);
    }
}
