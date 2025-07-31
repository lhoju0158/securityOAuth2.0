package com.exam.securityex01.config.auth;

import com.exam.securityex01.model.User;
import com.exam.securityex01.repository.UserRepository;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl ("/login");
// /login 요청이 오면 자동으로 UserDetailsService타입으로 IoC되어있는 loadUserByUsername 함수가 실행

@Service // IoC에 등록됨
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    // 시큐리티 session = Authentication = UserDetails
    // session (내부 Authentication (내부 UserDetails))

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username parameter는 기본적으로 username이다 만일 받는 값이 변경되면
        // SecurityConfig에 등록을 해야한다.
        // usernameParameter("username2"); -> 이런식으로
        User userEntity = userRepository.findByUsername(username);
        if(userEntity != null) {
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
