package aba3.lucid.schedule.Controller;


import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.schedule.dto.RegularHolidayRequest;
import aba3.lucid.domain.schedule.dto.RegularHolidayResponse;
import aba3.lucid.domain.schedule.repository.RegularHolidayRepository;
import aba3.lucid.schedule.Business.RegularHolidayBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class RegularHolidayController {
    private  final RegularHolidayRepository repository;
    private final RegularHolidayBusiness regularHolidayBusiness;



    @PostMapping("/regular")
    public API<RegularHolidayResponse> createRegular(
            @AuthenticationPrincipal CustomUserDetails company,
            @RequestBody RegularHolidayRequest regularHolidayRequest
    ) {
        RegularHolidayResponse response = regularHolidayBusiness.createHoliday(regularHolidayRequest, company.getCompanyId());
        return API.OK(response);
    }

    @PutMapping("/update/regularHoliday/{regId}")
    public API<RegularHolidayResponse> updateRegular(
            @AuthenticationPrincipal CustomUserDetails company,
            @PathVariable Long regId,
            @RequestBody RegularHolidayRequest regularHolidayRequest
    ){
        RegularHolidayResponse response = regularHolidayBusiness.updateHoliday (company.getCompanyId(), regId, regularHolidayRequest);
        return API.OK(response);
    }

    @GetMapping("/allRegularHoliday")
    public List<RegularHolidayResponse> getAllRegular() {
        return regularHolidayBusiness.findALlHolidays();

    }


}
