package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.comment.CommentRepository;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment commentWrite(String content, int imageId, int userId) {

        // 객체를 만들 때 id 값만 담아서 insert
        // return 시 image 와 user 객체는 id 값만 가지고있는 빈 객체
        Image image = new Image();
        image.setId(imageId);

        User userEntity = userRepository.findById(userId).orElseThrow(new Supplier<CustomApiException>() {
            @Override
            public CustomApiException get() {
                return new CustomApiException("해당 유저를 찾을 수 없습니다.");
            }
        });

        Comment commentEntity = new Comment();
        commentEntity.setContent(content);
        commentEntity.setImage(image);
        commentEntity.setUser(userEntity);

        return commentRepository.save(commentEntity);
    }

    @Transactional
    public void commentDelete(int id) {
        try {
            commentRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomApiException(e.getMessage());
        }
    }
}
