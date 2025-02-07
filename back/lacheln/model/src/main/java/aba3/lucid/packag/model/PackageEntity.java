package aba3.lucid.packag.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="package")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long packId;

    private Long pdId;

    private String name;

    private String comment;

    private String price;

    private String discountRate;

    private LocalDate startDate;

    private LocalDate endDate;

    private String image;



}
