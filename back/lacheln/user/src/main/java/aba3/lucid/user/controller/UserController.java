package aba3.lucid.user.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.common.image.ImageType;
import aba3.lucid.domain.user.dto.*;
import aba3.lucid.image.UserImageService;
import aba3.lucid.user.business.UserBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "유저 컨트롤러")
public class UserController {

    private final UserBusiness userBusiness;
    private final UserImageService userImageService;

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
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody UserUpdateRequest userUpdateRequest)   {
        return userBusiness.update(userUpdateRequest, user);
    }

    // 유저 프로필 조회
    @GetMapping("/profile")
    @Operation(summary = "소비자 조회", description = "소비자 정보를 조회합니다.")
    public API<UserCheckResponse> getUser(@AuthenticationPrincipal CustomUserDetails user)   {
        log.info("user.getUserId() : {}", user.getUserId());
        return userBusiness.getUser(user.getUserId());
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

    // 소비자 전체 불러오기
    @GetMapping("/all/{page}")
    @Operation(summary = "모든 소비자 조회", description = "모든 소비자의 정보를 반환합니다.")
    public API<UserAllResponse<UserInfoDto>> getAllUser(
            @PathVariable Integer page
    )   {
        return userBusiness.getAllUsers(page);
    }



    @GetMapping("/today_new_users")
    public API<Map<String, Object>> getTodayNewUsers() {
        long newUsers = userBusiness.getTodayNewUserCount();

        Map<String, Object> response = new HashMap<>();
        response.put("newUsers", newUsers);
        response.put("totalNewMembers", newUsers);

        return API.OK(response);
    }

    @GetMapping("/month_users")
    public API<List<Object[]>> getMonthUsers() {
        List<Object[]> stats = userBusiness.getMonthlyJoinStats();
        return API.OK(stats);
    }

    // 이미지 업로드
    @PostMapping("/image")
    public API<String> profileImage(
            @RequestParam("image") MultipartFile image,
            @AuthenticationPrincipal CustomUserDetails user
    ) throws IOException {
        String response = userImageService.profileImageUpload(image, user.getUserId(), ImageType.PROFILE);
        return API.OK(response);
    }
}
