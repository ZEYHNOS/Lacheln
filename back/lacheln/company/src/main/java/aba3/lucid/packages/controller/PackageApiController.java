package aba3.lucid.packages.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.packages.dto.PackageResponse;
import aba3.lucid.domain.packages.dto.PackageUserViewListResponse;
import aba3.lucid.packages.business.PackageBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/package")
@RequiredArgsConstructor
public class PackageApiController {

    private final PackageBusiness packageBusiness;

    @GetMapping("/list")
    public API<List<PackageUserViewListResponse>> getProductList() {
        List<PackageUserViewListResponse> packageUserViewListResponseList = packageBusiness.getPackageList();

        return API.OK(packageUserViewListResponseList);
    }

}
