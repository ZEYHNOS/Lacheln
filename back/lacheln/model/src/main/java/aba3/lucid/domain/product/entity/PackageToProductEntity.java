package aba3.lucid.domain.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Table(name = "package_mapping")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackageToProductEntity {
    // Package와 Product의 Mapping Table 고유키 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long packMapId;
    
    // 부모 테이블에서 데이터 제거시 매핑 테이블에서도 제거되도록 Cascade 적용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductEntity product;
    
    // 부모 테이블에서 데이터 제거시 매핑 테이블에서도 제거되도록 Cascade 적용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pack_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PackageEntity packageEntity;

}
