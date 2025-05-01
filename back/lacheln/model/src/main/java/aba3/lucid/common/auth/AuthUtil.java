package aba3.lucid.common.auth;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class AuthUtil {

    // ContextHolder에 담겨있는 인증정보를 가져와서 CustomAuthenticationToken으로 파싱값 반환
    public static CustomAuthenticationToken getAuth()   {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("auth , {}", auth);
        log.info("auth.isAuthenticated, {}", auth.isAuthenticated());
        log.info("auth instanceof CustomAuthenticationToken, {}", auth instanceof CustomAuthenticationToken);
        log.info("auth.getClass() {}", auth.getClass());
        if(auth.isAuthenticated()) {
            assert auth instanceof CustomAuthenticationToken;
            return (CustomAuthenticationToken) auth;
        }
        return null;
    }
    
    // 인증 정보를 가져와 Company Primary Key 값 반환
    public static Long getCompanyId() {
        CustomAuthenticationToken company = getAuth();
        return company != null ? company.getCompanyId() : null;
    }

    // 인증 정보를 가져와 User Primary Key 값 반환
    public static String getUserId()    {
        CustomAuthenticationToken user = getAuth();
        return user != null ? user.getUserId() : null;
    }
}
