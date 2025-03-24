package aba3.lucid.domain.packages.entity;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.dto.PackageUpdateRequest;
import aba3.lucid.domain.product.enums.PackageStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "package")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pack_id")
    private long packId; // 패키지 ID

    @Column(name = "pack_name", columnDefinition = "VARCHAR(255)", nullable = false)
    private String packName; // 패키지 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CompanyEntity packAdmin; // 패키지 방장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CompanyEntity packCompany1; // 패키지 업체 1

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CompanyEntity packCompany2; // 패키지 업체 2

    @Column(name = "pack_comment", columnDefinition = "TEXT", nullable = false)
    private String packComment; // 패키지 설명

    @Column(name = "pack_discountrate", columnDefinition = "INT", nullable = false)
    private int packDiscountrate; // 패키지 할인율

    @Column(name = "pack_create_date", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime packCreateDate; // 패키지 생성일

    @Column(name = "pack_end_date", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime packEndDate; // 패키지 종료일

    @Column(name = "pack_image_url", columnDefinition = "CHAR(255)", nullable = false)
    private String packImageUrl; // 패키지 대표 이미지

    @Enumerated(EnumType.STRING)
    @Column(name = "pack_status", columnDefinition = "CHAR(10)", nullable = false)
    private PackageStatus packStatus; // 패키지 상태(등록, 미등록, 삭제)


    @Column(name = "pack_del_date", columnDefinition = "DATETIME")
    private LocalDateTime packDelDate; // 패키지 삭제 일

    // TODO 상태 변경 로직
    public void updatePackageStatus(PackageStatus status) {
        Validator.throwIfNull(status);

        this.packStatus = status;
    }


    // 패키지에 업체 초대
    public void setPackageInvitationCompanies(CompanyEntity company1, CompanyEntity company2) {
        Validator.throwIfNull(company1, company2);

        if(isNotUniqueCompanyCategory(this.packAdmin, company1, company2)) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "서로 다른 카테고리의 업체여야 합니다.");
        }

        if (isNotDiffCompany(this.packAdmin, company1, company2)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }


        this.packCompany1 = company1;
        this.packCompany2 = company2;

    }


    private boolean isNotUniqueCompanyCategory(CompanyEntity packAdmin, CompanyEntity company1, CompanyEntity company2) {
        return packAdmin.getCpCategory() != company1.getCpCategory() &&
                company1.getCpCategory() != company2.getCpCategory() &&
                company2.getCpCategory() != packAdmin.getCpCategory();
    }


    private boolean isNotDiffCompany(CompanyEntity packAdmin, CompanyEntity company1, CompanyEntity company2) {
        return packAdmin.equals(company1) || company1.equals(company2) || company2.equals(packAdmin);
    }

    public void updatePackageName(String name) {
        Validator.assertStringValid(name, 5,  30);

        this.packName = name;
    }

    public void updatePackageComment(String packComment) {
        Validator.assertStringValid(packComment, 10, 255);

        this.packComment = packComment;
    }

    public void updatePackageEndDate(LocalDateTime endDate) {
        // 현재 날짜 + 1보다 커야함
        if (LocalDate.now().plusDays(1).isAfter(endDate.toLocalDate())) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER);
        }

        this.packEndDate = endDate;
    }

    public void updatePackageImageUrl(String url) {
        Validator.assertStringValid(url, 0, url.length());

        this.packImageUrl = url;
    }

    public void updatePackageDiscountrate(int packDiscountrate) {
        if (packDiscountrate < 0 || packDiscountrate > 100) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER);
        }

        this.packDiscountrate = packDiscountrate;
    }


    public void updateAdditionalField(PackageUpdateRequest request) {
        // TODO 상태 변경 로직
        updatePackageStatus(request.getStatus());
        updatePackageName(request.getName());
        updatePackageComment(request.getComment());
        updatePackageEndDate(request.getEndDate());
        updatePackageImageUrl(request.getImageUrl());
        updatePackageDiscountrate(request.getDiscountrate());

    }
}
