package aba3.lucid.domain.packages.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PackageResponse {

    private String name;

    private String comment;

    private int discountrate;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String imageUrl;

}
