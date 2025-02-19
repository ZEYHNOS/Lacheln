package aba3.lucid.domain.product;

import aba3.lucid.common.enums.BinaryChoice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    // ManyToOne
    private long packId;

    // ManyToOne
    private long cpId;

    // Yes Or No 선택지를 통해 패키지에 참가한 모든 업체가 Yes 상태일 때 패키지 등록하기
    @Enumerated(EnumType.STRING)
    @Column(name = "pack_cp_map_pass", columnDefinition = "CHAR(1)", nullable = false)
    private BinaryChoice packCpMapPass;
}
