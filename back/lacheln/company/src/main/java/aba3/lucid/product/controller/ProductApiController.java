package aba3.lucid.product.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.api.Pagination;
import aba3.lucid.common.api.PaginationConverter;
import aba3.lucid.domain.product.dto.ProductSearchRecord;
import aba3.lucid.domain.product.dto.option.ProductResponse;
import aba3.lucid.product.business.ProductBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductBusiness productBusiness;
    private final PaginationConverter paginationConverter;

    // 상품 리스트
    @GetMapping("/list")
    public API<List<ProductResponse>> getProductList(
            ProductSearchRecord productSearchRecord,
            @PageableDefault(size = 9, page = 0) Pageable pageable
    ) {
        Page<ProductResponse> productResponseList = productBusiness.getProductList(pageable, productSearchRecord);
        Pagination pagination = paginationConverter.createPagination(productResponseList, productSearchRecord.orderBy());
        return API.OK(productResponseList.stream().toList(), pagination);
    }

    // 상품 상세

}
