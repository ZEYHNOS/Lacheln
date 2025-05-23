package aba3.lucid.user.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.user.dto.*;
import aba3.lucid.user.business.UserBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;

@Slf4j
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
    @PutMapping("/update")
    @Operation(summary = "소비자 갱신", description = "소비자 정보를 수정합니다.")
    public API<UserUpdateResponse> updateUser(
            @RequestBody UserUpdateRequest userUpdateRequest)   {
        return userBusiness.update(userUpdateRequest);
    }

    // 유저 프로필 조회
    @GetMapping("/profile/{userId}")
    @Operation(summary = "소비자 조회", description = "소비자 정보를 조회합니다.")
    public API<UserCheckResponse> getUser(@PathVariable String userId)   {
        return userBusiness.getUser(userId);
    }

    //유저 프로필 이메일로 조회
    @GetMapping("/profile/email/{userEmail}")
    @Operation(summary = "소비자 조회", description = "소비자 정보를 조회합니다")
    public API<UserCheckResponse> getUserByEmail(@PathVariable String userEmail)   {
        return userBusiness.checkUserByEmail(userEmail);
    }

    // 암호 인증
    @PostMapping("/verify")
    @Operation(summary = "소비자 암호 검증", description = "소비자 프로필 수정과 같은 인증 요소가 필요할 때 암호를 검증하는 역할을 합니다.")
    public API<String> verifyUserByPassword(
            @RequestBody UserPasswordVerifyRequest userPasswordVerifyRequest
    ) {
        return userBusiness.getUserPasswordVerify(userPasswordVerifyRequest.getPassword());
    }

    // 소비자 타입 조회
    @GetMapping("/type/{userId}")
    @Operation(summary = "소비자 타입 조회", description = "소비자가 소셜, 로컬 로그인인지 정보를 반환합니다.")
    public API<String> getSocial(@PathVariable String userId)   {
        return userBusiness.getUserSocial(userId);
    }
}
