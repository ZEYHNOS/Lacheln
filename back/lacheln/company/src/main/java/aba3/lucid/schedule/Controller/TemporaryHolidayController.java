package aba3.lucid.schedule.Controller;


import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.schedule.dto.TemporaryHolidayRequest;
import aba3.lucid.domain.schedule.dto.TemporaryHolidayResponse;
import aba3.lucid.schedule.Business.TemporaryHolidayBusiness;
import aba3.lucid.schedule.Service.TemporaryHolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/company")
public class TemporaryHolidayController {
    private final TemporaryHolidayService temporaryHolidayService;
    private final TemporaryHolidayBusiness temporaryHolidayBusiness;

    @PostMapping("/temporary")
    public API<TemporaryHolidayResponse> createTemporaryHoliday(
            @AuthenticationPrincipal CustomUserDetails company,
            @RequestBody TemporaryHolidayRequest request
    ) {
        TemporaryHolidayResponse response = temporaryHolidayBusiness.createHoliday(request,company.getCompanyId());
        return API.OK(response);
    }

    @PutMapping("/update/temporary/{thId}")
    public API<TemporaryHolidayResponse> updateTemporaryHoliday(
            @AuthenticationPrincipal CustomUserDetails company,
            @PathVariable Long thId,
            @RequestBody TemporaryHolidayRequest request
    ) {
        TemporaryHolidayResponse response = temporaryHolidayBusiness.updateTemporaryHoliday(request, thId,company.getCompanyId());
        return API.OK(response);
    }
}
