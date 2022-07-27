package com.cos.photogramstart.domain.image;

import com.cos.photogramstart.domain.time.BaseTimeEntity;
import com.cos.photogramstart.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String caption; // 사진 설명
    private String postImageUrl; // 이미지 전송받아서 사진을 "서버" 특정 폴더에 저장 - DB에 그 저장된 경로를 INSERT

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    // 이미지 좋아요
    // 이미지 댓글
}
