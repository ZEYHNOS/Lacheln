package aba3.lucid.domain.packages.entity;

import aba3.lucid.domain.product.enums.PackageStatus;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "pack_comment", columnDefinition = "TEXT", nullable = false)
    private String packComment; // 패키지 설명

    @Column(name = "pack_discountrate", columnDefinition = "INT", nullable = false)
    private int packDiscountrate; // 패키지 할인율

    @Column(name = "pack_start_date", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime packStartdate; // 패키지 등록일

    @Column(name = "pack_end_date", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime packEndDate; // 패키지 종료일

    @Column(name = "pack_image_url", columnDefinition = "CHAR(255)", nullable = false)
    private String packImageUrl; // 패키지 대표 이미지

    @Column(name = "pack_status", columnDefinition = "CHAR(10)", nullable = false)
    private PackageStatus packStatus; // 패키지 상태(등록, 미등록, 삭제)




}
