package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.studio.convertor.StudioConverter;
import aba3.lucid.domain.product.studio.dto.StudioRequest;
import aba3.lucid.domain.product.studio.dto.StudioResponse;
import aba3.lucid.domain.product.studio.entity.StudioEntity;
import aba3.lucid.product.service.StudioService;

@Business
public class StudioBusiness extends ProductBusiness<StudioRequest, StudioResponse, StudioEntity> {


    public StudioBusiness(StudioService studioService
            , StudioConverter studioConverter
            , CompanyService companyService) {
        super(studioService, studioConverter, companyService);
    }

    @Override
    public CompanyCategory getCategory() {
        return CompanyCategory.S;
    }
}
