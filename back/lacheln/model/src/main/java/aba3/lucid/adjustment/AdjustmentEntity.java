package aba3.lucid.adjustment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "adjustment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdjustmentEntity {

    // 업체 id
    @Id
    private long cpId;

    // 은행 이름
    // TODO enum 처리하기
    private String bankName;

    // 은행 계좌번호
    private String bankAccount;

    // 예금주명
    private String depositName;

    // 정기 대금 수령일
    private LocalDate receiptDate;

}
