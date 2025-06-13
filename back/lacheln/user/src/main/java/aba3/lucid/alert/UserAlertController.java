package aba3.lucid.alert;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.alert.dto.UserAlertDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/alert")
@RequiredArgsConstructor
public class UserAlertController {

    private final UserAlertBusiness userAlertBusiness;

    @GetMapping("/list")
    public API<List<UserAlertDto>> getAlertList(
            @RequestBody CustomUserDetails user
    ) {
        List<UserAlertDto> userAlertDtoList =userAlertBusiness.getAlertListByUserId(user.getUserId());
        return API.OK(userAlertDtoList);
    }

    @GetMapping("/{alertId}")
    public void readAlertById(
            @PathVariable Long alertId
    ) {
        userAlertBusiness.readAlertById(alertId);
    }

    @DeleteMapping("/{alertId}")
    public void deleteById(
            @PathVariable Long alertId
    ) {
        userAlertBusiness.deleteAlertById(alertId);
    }

}
