package aba3.lucid.alert.controller;

import aba3.lucid.alert.business.CompanyAlertBusiness;
import aba3.lucid.alert.service.CompanyAlertService;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.alert.dto.CompanyAlertDto;
import aba3.lucid.domain.alert.dto.MutualAlert;
import aba3.lucid.domain.alert.entity.CompanyAlertEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company/alert")
public class CompanyAlertController {

    private final CompanyAlertBusiness companyAlertBusiness;

    @GetMapping("/list")
    public API<List<MutualAlert>> getAlertList(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<MutualAlert> companyAlertDtoList = companyAlertBusiness.getAlertList(user.getCompanyId());
        return API.OK(companyAlertDtoList);
    }

    @PutMapping("/read")
    public void readAlertList(
            @RequestBody List<Long> alertIdList
    ) {
        companyAlertBusiness.readAlert(alertIdList);
    }

    @DeleteMapping
    public void deleteAlertList(
            @RequestBody List<Long> alertIdList
    ) {
        companyAlertBusiness.deleteAlert(alertIdList);
    }

}
