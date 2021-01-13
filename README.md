# Spring-Security-Project1
구글, 페이스북, 카카오, 네이버 로그인 및 시큐리티를 이용한 권한 처리 예제

## Requirements
- JDK 1.8 ⬆
- Java 8 ⬆
- Maven 3.6.3 ⬆
- MySQL 5.7.3 ⬆
- Postman

## application.yml
```yml
server:
  port: 8080
  servlet:
    context-path: /
    encoding:
      charset: UTF-8
      enabled: true
      force: true

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/security?serverTimezone=Asia/Seoul
    username: cos
    password: cos1234

  thymeleaf:
    cache: false

  jpa:
    hibernate:
      ddl-auto: create #create update none
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    show-sql: true

  security:
    oauth2:
      client:
        registration:
          google:
            client-id: {Google-ID}
            client-secret: {Google-Secret}
            scope:
              - email
              - profile

          facebook:
            client-id: {Facebook-ID}
            client-secret: {Facebook-Secret}
            scope:
              - email
              - public_profile

          naver:
            client-id: {Naver-ID}
            client-secret: {Naver-Secret}
            scope:
              - name
              - email
              - profile_image
            client-name: Naver # 클라이언트 네임은 구글 페이스북도 대문자로 시작하더라.
            authorization-grant-type: authorization_code
            redirect-uri: http://localhost:8080/login/oauth2/code/naver
            
          kakao:
            client-id: {Kakao-ID}
            client-secret: {Kakao-Secret}
            scope:
              - profile
              - account_email
            client-name: Kakao
            client-authentication-method: POST
            authorization-grant-type: authorization_code
            redirect-uri: http://localhost:8080/login/oauth2/code/kakao

        # 네이버, 카카오는 OAuth2.0 공식 지원대상이 아니라서 provider 설정이 필요하다.
        # 요청주소도 다르고, 응답 데이터도 다르기 때문이다.
        # https://developers.naver.com/docs/login/devguide/#2-2-1-%EC%86%8C%EC%85%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8
        # https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api
        provider:
          naver:
            authorization-uri: https://nid.naver.com/oauth2.0/authorize
            token-uri: https://nid.naver.com/oauth2.0/token
            user-info-uri: https://openapi.naver.com/v1/nid/me
            user-name-attribute: response # 회원정보를 json의 response 키값으로 리턴해줌.

          kakao:
            authorization-uri: https://kauth.kakao.com/oauth/authorize
            token-uri: https://kauth.kakao.com/oauth/token
            user-info-uri: https://kapi.kakao.com/v2/user/me
            user-name-attribute: id # id로 안하면... 고생함
```
