package com.cos.security1.service;

import com.cos.security1.dto.UserSaveRequestDto;
import com.cos.security1.model.RoleType;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void save(UserSaveRequestDto dto) {

        System.out.println("UserService : save 호출");
        String rawPassword = dto.getPassword();
        String newPassword = bCryptPasswordEncoder.encode(rawPassword);
        dto.encodePassword(newPassword);
        dto.giveRole(RoleType.USER);
//        dto.giveRole(RoleType.MANAGER);
//        dto.giveRole(RoleType.ADMIN);

        userRepository.save(dto.toEntity());
    }
}
