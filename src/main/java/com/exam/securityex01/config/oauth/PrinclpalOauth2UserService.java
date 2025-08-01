package com.exam.securityex01.config.oauth;

import com.exam.securityex01.config.auth.PrincipalDetails;
import com.exam.securityex01.model.User;
import com.exam.securityex01.repository.UserRepository;
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

        // 강제 회원 가입 진행
        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        String password =bCryptPasswordEncoder.encode("비밀번호"); // oauth로 로그인 경우 굳이 필요 없음. 그냥 아무거나 진행
        String email = oAuth2User.getAttribute("email");
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
