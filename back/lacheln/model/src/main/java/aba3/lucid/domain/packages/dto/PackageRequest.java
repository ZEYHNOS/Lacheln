package aba3.lucid.domain.packages.dto;

import aba3.lucid.domain.product.enums.PackageStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PackageRequest {

    @NotBlank
    @Size(min = 5)
    private String name;

    @NotBlank
    @Size(min = 10)
    private String comment;

    private int discountrate;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String imageUrl;

    private PackageStatus status;


    public PackageStatus getStatusOrDefaultStatus() {
        if (status == null) {
            return PackageStatus.PRIVATE;
        }

        return this.status;
    }

    public LocalDateTime getStartDateOrDefaultDate() {
        if (this.startDate == null) {
            return LocalDateTime.now();
        }

        return this.startDate;
    }

    public LocalDateTime getEndDateOrDefaultDate() {
        if (this.endDate == null) {
            return LocalDateTime.of(2099, 12, 31, 0, 0, 0);
        }

        return this.endDate;
    }

    public String getImageUrlOrDefaultUrl() {
        if (this.imageUrl == null || this.imageUrl.trim().isEmpty()) {
            return "static/default/package_image.png";
        }

        return this.imageUrl;
    }
}
