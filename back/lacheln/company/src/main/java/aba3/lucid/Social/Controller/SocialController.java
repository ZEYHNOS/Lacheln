package aba3.lucid.Social.Controller;


import aba3.lucid.Social.Service.SocialService;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.company.dto.SnsRequest;
import aba3.lucid.domain.company.dto.SnsResponse;
import aba3.lucid.domain.company.repository.SocialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/social")
@RestController
@RequiredArgsConstructor
public class SocialController {
    private final SocialService socialService;
    private final SocialRepository socialRepository;

    @PostMapping("/create")
    public API<SnsResponse> createSocial(
            @AuthenticationPrincipal CustomUserDetails company,
            @RequestBody SnsRequest request
    ) {
        SnsResponse response = socialService.createSocial(request, company.getCompanyId());
        log.debug("Create Social: {}", response);
        return API.OK(response);
    }

    @PutMapping("/update")
    public API<SnsResponse> updateSocial(
            @AuthenticationPrincipal CustomUserDetails company,
            @RequestBody SnsRequest request
    ) {
        SnsResponse response = socialService.updateSocial(request,company.getCompanyId());
        return API.OK(response);
    }
}
