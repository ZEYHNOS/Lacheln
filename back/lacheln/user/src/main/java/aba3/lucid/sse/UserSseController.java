package aba3.lucid.sse;

import aba3.lucid.common.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("/user/sse")
@RequiredArgsConstructor
public class UserSseController {

    private final UserSseService sseService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(
            @AuthenticationPrincipal CustomUserDetails user
    ) throws IOException {
        return sseService.subscribe(user.getUserId());
    }
}
