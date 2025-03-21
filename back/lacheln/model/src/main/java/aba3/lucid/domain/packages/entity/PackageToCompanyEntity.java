package aba3.lucid.domain.packages.entity;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.company.entity.CompanyEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "package_cp_mapping")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackageToCompanyEntity {

    // 패키지 업체 매핑 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long packCpMapId;

    // 부모 테이블에서 데이터가 제거 될 시 해당 매핑테이블에 있는 데이터도 같이 삭제되도록 cascade 적용
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pack_id", nullable = false)
    private PackageEntity packageEntity;

    // 부모 테이블에서 데이터가 제거 될 시 해당 매핑테이블에 있는 데이터도 같이 삭제되도록 cascade 적용
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "cp_id", nullable = false)
    private CompanyEntity company;

    // Yes Or No 선택지를 통해 패키지에 참가한 모든 업체가 Yes 상태일 때 패키지 등록하기
    @Enumerated(EnumType.STRING)
    @Column(name = "pack_cp_map_pass", columnDefinition = "CHAR(1)", nullable = false)
    private BinaryChoice packCpMapPass;
}
