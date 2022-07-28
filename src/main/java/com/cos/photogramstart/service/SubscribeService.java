package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final EntityManager entityManager;

    @Transactional
    public void subscribed(int fromUserId, int toUserId) {
        try {
            subscribeRepository.mSubscribe(fromUserId, toUserId);
        } catch (Exception e) {
            throw new CustomApiException("이미 구독한 사용자입니다.");
        }

    }

    @Transactional
    public void unSubscribed(int fromUserId, int toUserId) {
        subscribeRepository.mUnSubscribe(fromUserId, toUserId);
    }

    @Transactional(readOnly = true)
    public List<SubscribeDto> subscribeList(int principalId, int pageUserId) {

        // 쿼리 준비
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT u.id, u.username, u.profileImageUrl, ");
        sb.append("IF((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id), 1, 0) AS subscribeState, ");
        sb.append("IF((? = u.id), 1, 0) AS equalUserState ");
        sb.append("FROM user u ");
        sb.append("INNER JOIN subscribe s ");
        sb.append("ON u.id = s.toUserId ");
        sb.append("WHERE s.fromUserId = ?");

        // 1. 물음표 principalId
        // 2. 물음표 principalId
        // 3. 마지막 물음표 pageUserId

        // 쿼리 완성
        Query query = entityManager.createNativeQuery(sb.toString())
                .setParameter(1, principalId)
                .setParameter(2, principalId)
                .setParameter(3, pageUserId);

        // 쿼리 실행
        JpaResultMapper result = new JpaResultMapper();
        List<SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class);

        return subscribeDtos;
    }
}
