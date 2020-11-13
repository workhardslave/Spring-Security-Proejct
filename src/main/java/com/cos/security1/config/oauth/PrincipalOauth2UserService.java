package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.config.oauth.provider.*;
import com.cos.security1.model.RoleType;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import com.cos.security1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;
    private final UserRepository userRepository;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리가 되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // registerationId로 어떤 OAuth로 로그인 했는지 확인
        System.out.println("userRequest.getClientRegistration() = " + userRequest.getClientRegistration());
        System.out.println("userRequest.getAccessToken().getTokenValue() = " + userRequest.getAccessToken().getTokenValue());

        // 구글 로그인 버튼 -> 구글 로그인 창 -> 로그인을 완료 -> code를 리턴(OAuth-Client 라이브러리) -> 액세스 토큰 요청
        // ===> userRequest 정보 -> 회원 프로필 받아야 함(loadUser 함수) -> 구글에서 회원 프로필 받아옴
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());

        // 소셜 로그인 분기점
        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            System.out.println("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            System.out.println("현재 구글, 페이스북, 네이버만 지원합니다. ㅠㅠ");
        }

        // 소셜 로그인을 통해 받은 정보를 토대로 회원가입 정보 가공
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail() + "_" + providerId;
        String username = oAuth2UserInfo.getName();
        String password = bCryptPasswordEncoder.encode("!@아무거나21");
        RoleType role = RoleType.USER;

        // 신규 회원인지 확인
        User user = userService.findByEmail(email);
        if(user.getEmail() == null) {
            System.out.println("소셜 로그인 최초");

            user = userRepository.save(user.builder()
                    .email(email)
                    .password(password)
                    .username(username)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build());

            System.out.println("user = " + user);
        } else {
            System.out.println("로그인을 이미 한 적 있습니다. 당신은 자동 회원가입이 되어 있습니다.");
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
