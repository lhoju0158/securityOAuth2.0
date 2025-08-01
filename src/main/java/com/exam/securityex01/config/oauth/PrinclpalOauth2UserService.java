package com.exam.securityex01.config.oauth;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

import com.exam.securityex01.config.auth.PrincipalDetails;
import com.exam.securityex01.config.oauth.provider.GoogleUserInfo;
import com.exam.securityex01.config.oauth.provider.NaverUserInfo;
import com.exam.securityex01.config.oauth.provider.OAuth2UserInfo;
import com.exam.securityex01.model.User;
import com.exam.securityex01.repository.UserRepository;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrinclpalOauth2UserService extends DefaultOAuth2UserService {
    // 구글로부터 받은 userRequest 데이터에 대한 후처리를 담당하는 함수
    // 함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어진다.

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAtttributes: " + oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;
        // 강제 회원 가입 진행
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        }
        else{
            System.out.println("지원하지 않는 페이지 입니다.");
        }
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password =bCryptPasswordEncoder.encode("비밀번호"); // oauth로 로그인 경우 굳이 필요 없음. 그냥 아무거나 진행
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);
        if(userEntity == null){
            // 강제로 회원가입 진행
            userEntity = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(role)
                .provider(provider)
                .providerId(providerId)
                .build();
            userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity,oAuth2User.getAttributes());
    }
}
