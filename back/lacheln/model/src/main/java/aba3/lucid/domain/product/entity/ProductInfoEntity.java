package aba3.lucid.domain.product.entity;

import aba3.lucid.domain.company.entity.CompanyCountryEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@Builder
@Table(name = "product_info")
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoEntity {

    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pdInfoId;

    // 외래키(상품)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_id")
    private ProductEntity product;

    // 외래키(업체언어)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_country_id")
    private CompanyCountryEntity companyCountry;

//    // 글
//    @OneToMany(mappedBy = "product_info", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ProductDescriptionEntity> productDescriptionEntityList;
}
