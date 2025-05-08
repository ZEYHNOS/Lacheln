package aba3.lucid.domain.description;

import aba3.lucid.common.enums.DescriptionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Description {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pdDescriptionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DescriptionType type;

    @Column(name = "block_value", nullable = false)
    private String value;

    @Column(name = "block_order")
    private int order;
}

