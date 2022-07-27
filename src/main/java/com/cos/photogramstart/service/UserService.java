package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    public UserProfileDto userProfile(int pageUserId, int principalId) {

        UserProfileDto dto = new UserProfileDto();
        User userEntity = userRepository.findById(pageUserId).orElseThrow(new Supplier<CustomException>() {
            @Override
            public CustomException get() {
                return new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
            }
        });

        dto.setUser(userEntity);
        dto.setPageOwnerState(pageUserId == principalId); // 1은 페이지 주인, -1은 주인이 아닌
        dto.setImageCount(userEntity.getImages().size());

        return dto;
    }

    @Transactional
    public User userUpdate(int id, User user) {
        // 1.영속화
        User userEntity = userRepository.findById(id).orElseThrow(new Supplier<CustomValidationApiException>() {
            @Override
            public CustomValidationApiException get() {
                return new CustomValidationApiException("해당 아이디는 존재하지 않습니다. : " + id);
            }
        });

        // 2.영속화된 오브젝트 수정 - 더티체킹(업데이트 완료)
        userEntity.setName(user.getName());

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        userEntity.setPassword(encPassword);

        userEntity.setWebsite(user.getWebsite());
        userEntity.setBio(user.getBio());
        userEntity.setPhone(user.getPhone());
        userEntity.setGender(user.getGender());

        return userEntity;
    }
}
