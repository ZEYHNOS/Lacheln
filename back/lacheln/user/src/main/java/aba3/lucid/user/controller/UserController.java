package aba3.lucid.user.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.user.business.UserBusiness;
import aba3.lucid.domain.user.dto.UserSignupRequest;
import aba3.lucid.domain.user.dto.UserSignupResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "유저 컨트롤러")
public class UserController {

    private final UserBusiness userBusiness;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "로컬 환경에서의 회원가입을 진행합니다.")
    public API<UserSignupResponse> createUser(
            @RequestBody UserSignupRequest userSignupRequest) {
        return userBusiness.signup(userSignupRequest);
    }

    @GetMapping("/test")
    @ResponseBody
    public String getUser() {
        System.out.println(AuthUtil.getUserId());
        return AuthUtil.getUserId();
    }
}
