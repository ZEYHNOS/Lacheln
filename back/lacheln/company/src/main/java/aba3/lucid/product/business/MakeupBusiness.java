package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.makeup.convertor.MakeUpConverter;
import aba3.lucid.domain.product.makeup.dto.MakeUpResponse;
import aba3.lucid.domain.product.makeup.dto.MakeupRequest;
import aba3.lucid.domain.product.makeup.entity.MakeupEntity;
import aba3.lucid.product.service.MakeupService;

import java.util.List;

@Business
public class MakeupBusiness extends ProductBusiness<MakeupRequest, MakeUpResponse, MakeupEntity> {

    private final MakeUpConverter makeUpConverter;
    private final MakeupService makeupService;

    public MakeupBusiness(MakeupService makeupService, MakeUpConverter makeUpConverter, CompanyService companyService) {
        super(makeupService, makeUpConverter, companyService);
        this.makeupService = makeupService;
        this.makeUpConverter = makeUpConverter;
    }


    public List<MakeUpResponse> getProductList(long companyId) {
        return makeupService.getProductList(companyId).stream()
                .map(makeUpConverter::toResponse)
                .toList()
                ;
    }

    @Override
    public CompanyCategory getCategory() {
        return CompanyCategory.M;
    }

}
