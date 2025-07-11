package aba3.lucid.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PopularDto implements Serializable {

    private Long productId;

    private Long companyId;

    private Integer rank;

}
