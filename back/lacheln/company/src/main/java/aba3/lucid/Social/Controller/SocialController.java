package aba3.lucid.Social.Controller;


import aba3.lucid.Social.Service.SocialService;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.company.dto.SnsRequest;
import aba3.lucid.domain.company.dto.SnsResponse;
import aba3.lucid.domain.company.repository.SocialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/social")
@RestController
@RequiredArgsConstructor
public class SocialController {
    private final SocialService socialService;
    private final SocialRepository socialRepository;

    @PostMapping("{companyId}/create")
    public API<SnsResponse> createSocial(
            @PathVariable Long companyId,
            @RequestBody SnsRequest request
    ) {
        SnsResponse response = socialService.createSocial(request, AuthUtil.getCompanyId());
        log.debug("Create Social: {}", response);
        return API.OK(response);
    }

    @PutMapping("{companyId}")
    public API<SnsResponse> updateSocial(
            @PathVariable Long companyId,
            @RequestBody SnsRequest request
    ) {
        SnsResponse response = socialService.updateSocial(request,AuthUtil.getCompanyId());
        return API.OK(response);
    }
}
