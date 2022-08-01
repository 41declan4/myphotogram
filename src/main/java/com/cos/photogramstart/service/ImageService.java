package com.cos.photogramstart.service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    private final ImageRepository imageRepository;

    @Value("${file.path}") // yml의 file.path 경로
    private String uploadFolder;

    @Transactional
    public void imageUpload(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
        UUID uuid = UUID.randomUUID(); // 고유성이 보장되는 id
        String imageFileName = uuid + "_" + imageUploadDto.getFile().getOriginalFilename(); // 실제 파일 이름 + uuid
        logger.info("이미지 파일 이름 : " + imageFileName);

        Path imageFilePath = Paths.get(uploadFolder + imageFileName); // 실제 경로 저장

        // 통신, I/O -> 예외 발생 가능성
        try {
            Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // image 테이블 저장
        Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());
        imageRepository.save(image);
    }

    @Transactional(readOnly = true)
    public Page<Image> imageStory(int principalId, Pageable pageable) {
        Page<Image> images = imageRepository.mStory(principalId, pageable);

        // images에 좋아요 상태 담기
        images.forEach((image) -> {

            image.setLikeCount(image.getLikes().size());

            image.getLikes().forEach((like) -> {
                if (like.getUser().getId() == principalId) { // 해당 이미지에 좋아요한 사람들을 찾아서 현재 로그인한 사람이 좋아요 한것인지 비교
                    image.setLikeState(true);
                }
            });
        });

        return images;
    }

    @Transactional(readOnly = true)
    public List<Image> popularImg() {
        return imageRepository.mPopular();
    }

}
