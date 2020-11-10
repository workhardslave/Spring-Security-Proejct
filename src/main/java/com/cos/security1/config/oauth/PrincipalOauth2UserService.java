package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.dto.UserSaveRequestDto;
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

        // 구글로 받은 정보를 토대로 회원가입 정보 가공
        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email") + "_" + providerId;
        String username = oAuth2User.getAttribute("name");
        String password = bCryptPasswordEncoder.encode("!@아무거나21");
        RoleType role = RoleType.USER;

        // 신규 회원인지 확인
        User user = userService.findByEmail(email);
        if(user.getEmail() == null) {
            System.out.println("구글 로그인 최초");
            UserSaveRequestDto dto = new UserSaveRequestDto();
            dto.oauthInfo(email, password, username, role, provider, providerId); // 구글 정보 -> dto
            user = dto.toEntity(); // dto -> entity
            System.out.println("user = " + user);
            userRepository.save(user);
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
