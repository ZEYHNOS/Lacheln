package aba3.lucid.service;

import aba3.lucid.config.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final RestTemplate restTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

//        if("kakao".equals(userRequest.getClientRegistration().getClientName())) {
//            return loadKakaoUser(userRequest);
//        }

        System.out.println("==loadUser Start==");
        System.out.println("getClientRegistration: "+userRequest.getClientRegistration());
        System.out.println("getAccessToken: "+userRequest.getAccessToken());
        System.out.println("getAttributes: "+super.loadUser(userRequest).getAttributes());
        System.out.println("==loadUser End==");

        return super.loadUser(userRequest);
    }

    private OAuth2User loadKakaoUser(OAuth2UserRequest userRequest) {
        String accessToken = userRequest.getAccessToken().getTokenValue();
        String uri = "https://kapi.kakao.com/v2/user/me";
        Map<String, Object> kakaoUserInfo;
        System.out.println(accessToken);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    new HttpEntity<>(createHeaders(accessToken)),
                    Map.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new OAuth2AuthenticationException("카카오 API 요청 실패: " + response.getStatusCode());
            }

            kakaoUserInfo = response.getBody();
        } catch (RestClientException e) {
            throw new OAuth2AuthenticationException(e.getMessage());
        }

        Map<String, Object> properties = (Map<String, Object>) kakaoUserInfo.get("properties");
        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoUserInfo.get("kakao_account");

        String id = kakaoUserInfo.get("id").toString();
        String nickname = (String) properties.get("nickname");
        String email = (String) kakaoAccount.get("email");

        // OAuth2User 객체 반환 (여기서 사용자 정보를 반환)
        return new CustomOAuth2User(id, nickname, email);
    }

    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.SET_COOKIE, accessToken);
        return headers;
    }
}
