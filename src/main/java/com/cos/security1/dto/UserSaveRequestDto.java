package com.cos.security1.dto;

import com.cos.security1.model.RoleType;
import com.cos.security1.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSaveRequestDto {

    private String email;
    private String password;
    private String username;
    private String phone;
    private RoleType role;

    public void giveRole(RoleType role) {
        this.role = role;
    }

    public void encodePassword(String password) {
        this.password = password;
    }

    // User의 builder를 통해 dto -> entity
    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .username(username)
                .phone(phone)
                .role(role)
                .build();
    }
}
