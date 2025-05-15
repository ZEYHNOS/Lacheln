package aba3.lucid.domain.product.makeup.entity;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.makeup.dto.MakeupRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@Table(name = "makeup")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("makeup")
public class MakeupEntity extends ProductEntity {

    // 출장 여부
    @Column(name = "makeup_bt", nullable = false)
    @Enumerated(EnumType.STRING)
    private BinaryChoice makeupBt;

    // 방문 여부
    @Column(name = "makeup_visit", nullable = false)
    @Enumerated(EnumType.STRING)
    private BinaryChoice makeupVisit;

    // 담당자
    @Column(name = "makeup_manager", nullable = false, columnDefinition = "VARCHAR(10)")
    private String makeupManager;


    public void setBusinessTrip(BinaryChoice businessTrip) {
        Validator.throwIfNull(businessTrip);
        this.makeupBt = businessTrip;
    }

    public void setVisit(BinaryChoice visit) {
        Validator.throwIfNull(visit);
        this.makeupVisit = visit;
    }

    public void setManager(String makeupManager) {
        Validator.throwIfNull(makeupManager);
        this.makeupManager = makeupManager;
    }

    public void setAdditionalField(MakeupRequest request) {
        Validator.throwIfNull(request);

        setProductName(request.getName());
        setTaskTime(request.getTaskTime());
        setRec(request.getRec());
        setBusinessTrip(request.getBusinessTrip());
        setVisit(request.getVisit());
        setManager(request.getManager());
    }
}
