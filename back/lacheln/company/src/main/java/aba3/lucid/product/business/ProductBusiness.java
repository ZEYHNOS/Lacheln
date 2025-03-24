package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.converter.ProductConverter;
import aba3.lucid.domain.product.dto.option.ProductResponse;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.product.service.ProductService;
import aba3.lucid.rabbitmq.Producer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class ProductBusiness {

    private final ProductService productService;

    private final ProductConverter productConverter;

    private final Producer producer;

    public List<ProductResponse> getProductList(CompanyCategory category, int minimum, int maximum, boolean isDesc) {
        List<ProductEntity> productEntityList = productService.getProductList(category, minimum, maximum, isDesc);

        return productConverter.toResponseList(productEntityList);
    }

    public void sendMessage(String message) {
        producer.sendMessage(message);
    }




}
