package aba3.lucid.detailoption.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name="detail_option")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailOpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long detailOpId;

    private Long opId;

    private String detailOpName;

    private int opPlusPrice;

}
