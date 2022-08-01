package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SubscribeRepository subscribeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${file.path}") // yml의 file.path 경로
    private String uploadFolder;

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

        int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
        int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);

        dto.setSubscribeState(subscribeState == 1);
        dto.setSubscribeCount(subscribeCount);

        userEntity.getImages().forEach((image -> {
            image.setLikeCount(image.getLikes().size());
        }));

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

    @Transactional
    public User userProfileImageModify(int principalId, MultipartFile profileImageFile) {

        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + profileImageFile.getOriginalFilename();

        Path imageFilePath = Paths.get(uploadFolder + imageFileName);

        try {
            Files.write(imageFilePath, profileImageFile.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        User userEntity = userRepository.findById(principalId).orElseThrow(new Supplier<CustomApiException>() {
            @Override
            public CustomApiException get() {
                return new CustomApiException("유저를 찾을 수 없습니다.");
            }
        });
        userEntity.setProfileImageUrl(imageFileName);

        return userEntity;
    }
}
