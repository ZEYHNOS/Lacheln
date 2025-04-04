package aba3.lucid.user.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.user.dto.*;
import aba3.lucid.user.business.UserBusiness;
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
    @Operation(summary = "소비자 회원가입", description = "로컬 환경에서의 소비자 회원가입을 진행합니다.")
    public API<UserSignupResponse> createUser(
            @RequestBody UserSignupRequest userSignupRequest) {
        return userBusiness.signup(userSignupRequest);
    }

    // 유저 업데이트 컨트롤러
    @PostMapping("/update")
    @Operation(summary = "소비자 갱신", description = "소비자 정보를 수정합니다.")
    public API<UserUpdateResponse> updateUser(
            @RequestBody UserUpdateRequest userUpdateRequest)   {
        return userBusiness.update(userUpdateRequest);
    }

    // 유저 프로필 조회
    @GetMapping("/profile")
    @Operation(summary = "소비자 조회", description = "소비자 정보를 조회합니다.")
    public API<UserCheckResponse> getUser()   {
        return userBusiness.getUser();
    }

    // 암호 인증
    @PostMapping("/verify")
    @Operation(summary = "소비자 암호 검증", description = "소비자 프로필 수정과 같은 인증 요소가 필요할 때 암호를 검증하는 역할을 합니다.")
    public API<String> verifyUserByPassword(
            @RequestBody UserPasswordVerifyRequest userPasswordVerifyRequest
    ) {
        return userBusiness.getUserPasswordVerify(userPasswordVerifyRequest.getPassword());
    }

    // 컨텍스트 유저 추출 테스트 코드
    @GetMapping("/test")
    @ResponseBody
    public String testUser() {
        System.out.println(AuthUtil.getUserId());
        return AuthUtil.getUserId();
    }
}
