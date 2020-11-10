package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/signin")
// /signin 요청이 오면 자동으로 UserDetailsSerivce 타입으로 IoC 되어있는 loadUserByUsername 함수가 실행
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserService userService;

    // Security Session(내부 Authentication(내부 UserDeatils))
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        System.out.println("email = " + email);

        User principal = userService.findByEmail(email);

        return new PrincipalDetails(principal);
    }
}
