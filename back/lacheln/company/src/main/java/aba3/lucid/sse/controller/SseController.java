package aba3.lucid.sse.controller;

import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sse/company")
public class SseController {

    private final SseService sseService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(
            @AuthenticationPrincipal CustomUserDetails user
    ) throws IOException {
        return sseService.subscribe(user.getCompanyId());
    }
}
