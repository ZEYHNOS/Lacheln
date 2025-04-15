package aba3.lucid.schedule.Controller;


import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.schedule.dto.RegularHolidayRequest;
import aba3.lucid.domain.schedule.dto.RegularHolidayResponse;
import aba3.lucid.domain.schedule.repository.RegularHolidayRepository;
import aba3.lucid.schedule.Business.RegularHolidayBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class RegularHolidayController {
    private  final RegularHolidayRepository repository;
    private final RegularHolidayBusiness regularHolidayBusiness;

    @PostMapping("{cpId}/regular")
    public API<RegularHolidayResponse> createRegular(
            @PathVariable Long cpId,
            @RequestBody RegularHolidayRequest regularHolidayRequest
    ) {
        RegularHolidayResponse response = regularHolidayBusiness.createHoliday(regularHolidayRequest, AuthUtil.getCompanyId());
        return API.OK();
    }


}
