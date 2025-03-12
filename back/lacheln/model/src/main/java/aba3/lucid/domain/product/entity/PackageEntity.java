package aba3.lucid.domain.product.entity;

import aba3.lucid.domain.product.enums.PackageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "package")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pack_id")
    private long packId;

    @Column(name = "pack_name", columnDefinition = "VARCHAR(255)", nullable = false)
    private String packName;

    @Column(name = "pack_comment", columnDefinition = "TEXT", nullable = false)
    private String packComment;

    @Column(name = "pack_discountrate", columnDefinition = "INT", nullable = false)
    private int packDiscountrate;

    @Column(name = "pack_start_date", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime packStartdate;

    @Column(name = "pack_end_date", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime packEndDate;

    @Column(name = "pack_image_url", columnDefinition = "CHAR(255)", nullable = false)
    private String packImageUrl;

    @Column(name = "pack_status", columnDefinition = "CHAR(10)", nullable = false)
    private PackageStatus packStatus;
}
