package com.cos.photogramstart.domain.subscribe;

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
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "subscribe_uk",
                        columnNames = {"fromUserId", "toUserId"} // 실제 데이터베이스 컬럼명
                )
        }
)
public class Subscribe extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "fromUserId")
    @ManyToOne
    private User fromUser; // 구독하는

    @JoinColumn(name = "toUserId")
    @ManyToOne
    private User toUser; // 구독받는
}
