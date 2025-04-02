package aba3.lucid.user.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.user.convertor.UserConvertor;
import aba3.lucid.domain.user.dto.UserSignupRequest;
import aba3.lucid.domain.user.dto.UserSignupResponse;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class UserBusiness {
    private final UserConvertor userConvertor;
    private final UserService userService;

    public API<UserSignupResponse> signup(UserSignupRequest userSignupRequest) {
        if(userSignupRequest == null)   {
            return API.ERROR(ErrorCode.GONE, "요청에 대한 데이터가 없습니다.");
        }
        if(!userSignupRequest.isEmailVerified() && userSignupRequest.isPhoneVerified()) {
            return API.ERROR(ErrorCode.BAD_REQUEST, "이메일, 휴대전화 인증을 진행해주세요.");
        }

        UsersEntity convertedUser = userConvertor.convertUserSignupToUserEntity(userSignupRequest);
        UsersEntity savedUser = userService.signUp(convertedUser);

        UserSignupResponse userSignupResponse = UserSignupResponse.builder()
                .data(savedUser)
                .message("회원가입에 성공하였습니다!")
                .build();

        return API.OK(userSignupResponse);
    }
}
