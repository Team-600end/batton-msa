package com.batton.noticeservice.repository;

import com.batton.noticeservice.domain.Notice;
import com.batton.noticeservice.enums.NoticeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    // 수신자 아이디 + 생성시간 내림차순으로 Notice 조회
    List<Notice> findAllByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    List<Notice> findTop4ByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    List<Notice> findAllByReceiverIdAndNoticeTypeInOrderByCreatedAtDesc(Long receiverId, List<NoticeType> types);

    List<Notice> findTop4ByReceiverIdAndNoticeTypeInOrderByCreatedAtDesc(Long receiverId, List<NoticeType> types);
}
