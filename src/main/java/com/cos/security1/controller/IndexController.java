package com.cos.security1.controller;

import com.cos.security1.dto.UserSaveRequestDto;
import com.cos.security1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final UserService userService;

    // localhost:8080, localhost:8080/
    @GetMapping({"", "/"})
    public String index() {

        // thymeleaf 기본 폴더 : src/main/resources/
        // view resolver 설정 : templates(prefix), html(suffix) 생략 가능
        return "index";
    }

    @GetMapping("/user")
    public String user() {

        return "user";
    }

    @GetMapping("/admin")
    public String admin() {

        return "admin";
    }

    @GetMapping("/manager")
    public String manager() {

        return "manager";
    }

    @GetMapping("/signin")
    public String signin() {

        return "signin";
    }


    @GetMapping("/signup")
    public String signup() {

        return "signup";
    }

    @PostMapping("/signup")
    public String signup(UserSaveRequestDto dto) {

        System.out.println("IndexController : signup 호출");
        userService.save(dto);

        return "redirect:/signin";

    }

    @GetMapping("/signout")
    public String signout() {

        return "signout";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {

        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {

        return "데이터";
    }
}
