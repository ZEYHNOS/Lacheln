package aba3.lucid.schedule.Controller;


import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.schedule.dto.TemporaryHolidayRequest;
import aba3.lucid.domain.schedule.dto.TemporaryHolidayResponse;
import aba3.lucid.schedule.Business.TemporaryHolidayBusiness;
import aba3.lucid.schedule.Service.TemporaryHolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/company")
public class TemporaryHolidayController {
    private final TemporaryHolidayService temporaryHolidayService;
    private final TemporaryHolidayBusiness temporaryHolidayBusiness;

    @PostMapping("/{cpId}/temporary")
    public API<TemporaryHolidayResponse> createTemporaryHoliday(
            @PathVariable Long cpId,
            @RequestBody TemporaryHolidayRequest request
    ) {
        TemporaryHolidayResponse response = temporaryHolidayBusiness.createHoliday(request, AuthUtil.getCompanyId());
        return API.OK(response);
    }

    @PutMapping("{cpId}/{thId}/temporary")
    public API<TemporaryHolidayResponse> updateTemporaryHoliday(
            @PathVariable Long cpId,
            @PathVariable Long thId,
            @RequestBody TemporaryHolidayRequest request
    ) {
        TemporaryHolidayResponse response = temporaryHolidayBusiness.updateTemporaryHoliday(request, thId, AuthUtil.getCompanyId());
        return API.OK(response);
    }
}
