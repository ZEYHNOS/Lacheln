package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.makeup.convertor.MakeUpConverter;
import aba3.lucid.domain.product.makeup.dto.MakeUpResponse;
import aba3.lucid.domain.product.makeup.dto.MakeupRequest;
import aba3.lucid.domain.product.makeup.entity.MakeupEntity;
import aba3.lucid.product.service.MakeupService;

@Business
public class MakeupBusiness extends ProductAbstractBusiness<MakeupRequest, MakeUpResponse, MakeupEntity> {

    public MakeupBusiness(MakeupService makeupService, MakeUpConverter makeUpConverter, CompanyService companyService) {
        super(makeupService, makeUpConverter, companyService);
    }

    @Override
    public CompanyCategory getCategory() {
        return CompanyCategory.M;
    }

}
