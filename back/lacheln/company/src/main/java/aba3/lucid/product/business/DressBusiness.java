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

import java.util.List;

@Slf4j
@Business
public class DressBusiness extends ProductBusiness<DressRequest, DressResponse, DressEntity> {

    private final DressConverter dressConverter;
    private final DressService dressService;

    public DressBusiness(DressService dressService
            , DressConverter dressConverter
            , CompanyService companyService) {
        super(dressService, dressConverter, companyService);
        this.dressConverter = dressConverter;
        this.dressService = dressService;
    }

    @Override
    public List<DressResponse> getProductList(long companyId) {
        return dressService.getProductList(companyId).stream()
                .map(dressConverter::toResponse)
                .toList()
                ;
    }

    @Override
    public CompanyCategory getCategory() {
        return CompanyCategory.D;
    }
}
