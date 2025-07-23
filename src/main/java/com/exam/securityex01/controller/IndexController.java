package com.exam.securityex01.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // view를 리턴하겠다는 의미
public class IndexController {
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

    // 스프링 시큐리티가 해당 주소를 낚아챔
    @GetMapping("/login")
    public @ResponseBody String login(){
        return "login";
    }

    @GetMapping("/join")
    public @ResponseBody String join(){
        return "join";
    }
    @GetMapping("/joinnProc")
    public @ResponseBody String joinnProc(){
        return "회원가입 완료됨";
    }

}
