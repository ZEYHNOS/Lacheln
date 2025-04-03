package aba3.lucid.user.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.user.dto.UserUpdateRequest;
import aba3.lucid.domain.user.dto.UserUpdateResponse;
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

    // 유저 회원가입 컨트롤러
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "로컬 환경에서의 회원가입을 진행합니다.")
    public API<UserSignupResponse> createUser(
            @RequestBody UserSignupRequest userSignupRequest) {
        return userBusiness.signup(userSignupRequest);
    }

    // 유저 업데이트 컨트롤러
    @PostMapping("/update")
    @Operation(summary = "유저 갱신", description = "회원 정보를 수정합니다.")
    public API<UserUpdateResponse> updateUser(
            @RequestBody UserUpdateRequest userUpdateRequest)   {
        return userBusiness.update(userUpdateRequest);
    }

    // 컨텍스트 유저 추출 테스트 코드
    @GetMapping("/test")
    @ResponseBody
    public String getUser() {
        System.out.println(AuthUtil.getUserId());
        return AuthUtil.getUserId();
    }
}
