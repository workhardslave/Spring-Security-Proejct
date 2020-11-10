package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.dto.UserSaveRequestDto;
import com.cos.security1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final UserService userService;

    @GetMapping("/test/signin")
    public @ResponseBody String testSignIn(Authentication authentication,
                                           @AuthenticationPrincipal PrincipalDetails userDetails) {
        System.out.println("/test/signin =======================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principalDetails.getUser() = " + principalDetails.getUser());
        System.out.println("userDetails.getUser() = " + userDetails.getUser());

        return "세션 정보 확인";
    }

    @GetMapping("/test/oauth/signin")
    public @ResponseBody String testAuthSignIn(Authentication authentication,
                                           @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("/test/oauth/signin =======================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());
        System.out.println("oauth.getAttributes() = " + oauth.getAttributes());

        return "OAuth 세션 정보 확인";
    }

    // localhost:8080, localhost:8080/
    @GetMapping({"", "/"})
    public String index() {

        // thymeleaf 기본 폴더 : src/main/resources/
        // view resolver 설정 : templates(prefix), html(suffix) 생략 가능
        return "index";
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principal) {

        System.out.println("principal = " + principal.getUser());
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
