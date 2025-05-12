package aba3.lucid.domain.product.studio.entity;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.studio.dto.StudioRequest;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "studio")
@SuperBuilder
@DiscriminatorValue("studio")
public class StudioEntity extends ProductEntity{

    // 실내촬영여부
    @Column(name = "std_in_available", nullable = false)
    private BinaryChoice stdInAvailable;

    // 야외촬영여부
    @Column(name = "std_out_available", nullable = false)
    private BinaryChoice stdOutAvailable;

    // 최대수용인원
    @Column(name = "std_max_people", nullable = false)
    private int stdMaxPeople;

    // 배경선택여부
    @Column(name = "std_bg_options", nullable = false)
    private BinaryChoice stdBgOptions;


    public void updateInAvailable(BinaryChoice stdInAvailable) {
        Validator.throwIfNull(stdInAvailable);

        this.stdInAvailable = stdInAvailable;
    }

    public void updateOutAvailable(BinaryChoice stdOutAvailable) {
        Validator.throwIfNull(stdOutAvailable);

        this.stdOutAvailable = stdOutAvailable;
    }

    public void updateMaxPeople(int n) {
        if (n <= 0) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER);
        }

        this.stdMaxPeople = n;
    }

    public void updateBackGroundOptions(BinaryChoice stdBgOptions) {
        Validator.throwIfNull(stdBgOptions);

        this.stdBgOptions = stdBgOptions;
    }


    public void updateAdditionalField(StudioRequest request) {
        Validator.throwIfNull(request);

        updateProductName(request.getName());
        updateStatus(request.getStatus());
        updateTaskTime(request.getTaskTime());
        updateRec(request.getRec());
        updateInAvailable(request.getInAvailable());
        updateOutAvailable(request.getOutAvailable());
        updateMaxPeople(request.getMaxPeople());
        updateBackGroundOptions(request.getBgOptions());
    }
}
