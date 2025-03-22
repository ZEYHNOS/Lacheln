package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.dress.convertor.DressConverter;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.dto.DressResponse;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.product.service.DressService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Business
public class DressBusiness extends ProductAbstractBusiness<DressRequest, DressResponse, DressEntity> {

    public DressBusiness(DressService dressService
            , DressConverter dressConverter
            , CompanyService companyService) {
        super(dressService, dressConverter, companyService);
    }

    @Override
    public CompanyCategory getCategory() {
        return CompanyCategory.D;
    }
}
