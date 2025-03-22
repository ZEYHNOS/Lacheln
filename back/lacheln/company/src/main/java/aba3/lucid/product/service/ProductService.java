package aba3.lucid.product.service;

import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public List<ProductEntity> getProductList(CompanyCategory category, int minimum, int maximum, boolean isDesc) {
        Sort sort = isDesc ? Sort.by(Sort.Order.desc("id")) : Sort.by(Sort.Order.asc("id"));

        return List.of();
    }

}
