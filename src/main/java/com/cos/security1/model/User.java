package com.cos.security1.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@ToString
@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라감
    private int id; // sequence, auto_increment

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 100) // 123456 => 해쉬(패스워드 암호화)
    private String password;

    @Column(nullable = false, length = 30)
    private String username;

    //    @ColumnDefault(("'user'"))
    // DB는 RoleType 타입이 없으므로 String이라는 것을 명시
    @Enumerated(EnumType.STRING)
    private RoleType role; // USER, MANAGER, ADMIN

    private String provider;

    private String providerId;

    @CreationTimestamp // 시간이 자동으로 입력
    private Timestamp createDate;

    public String getRoleValue() {
        return this.role.getValue();
    }

    @Builder
    public User(String email, String password, String username, RoleType role, String provider, String providerId) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}
