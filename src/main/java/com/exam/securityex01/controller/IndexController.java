package com.exam.securityex01.controller;
import com.exam.securityex01.model.User;
import com.exam.securityex01.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // view를 리턴하겠다는 의미
public class IndexController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping({"","/"})
    public String index(){
        // mustache의 기본 폴더: src/main/resource
        // 뷰리졸버 설정: templates (prefix), mustache (suffix) 생략 가능

        return "index";

        // 기본적으로 src/main/resource/index.mustache를 찾는다 -> 이걸 index.html를 찾게 변경해야함
    }
    @GetMapping("/user")
    public @ResponseBody String user(){
        return "user";
    }
    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    // 스프링 시큐리티가 해당 주소를 낚아챔
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @PostMapping("/join")
    public String join(User user){
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        // 단순하게 userRepository.save(user);로 저장하면 security로 로그인이 불가하다.
        // password가 암호화 되어야 로그인이 가능하다.
        System.out.println(user);
        return "redirect:/loginForm";
    }


}
