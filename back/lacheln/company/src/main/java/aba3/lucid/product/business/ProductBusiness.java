package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.converter.ProductConverter;
import aba3.lucid.domain.product.dto.option.ProductResponse;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class ProductBusiness {

    private final ProductService productService;

    private final ProductConverter productConverter;

    public List<ProductResponse> getProductList(CompanyCategory category, int minimum, int maximum, boolean isDesc) {
        List<ProductEntity> productEntityList = productService.getProductList(category, minimum, maximum, isDesc);

        return productConverter.toResponseList(productEntityList);
    }

}
