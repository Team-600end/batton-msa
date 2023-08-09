package com.batton.noticeservice.mq;

import com.batton.noticeservice.domain.Notice;
import com.batton.noticeservice.mq.dto.NoticeMessage;
import com.batton.noticeservice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitConsumer {
    private final NoticeRepository noticeRepository;

    @RabbitListener(queues = "notice.queue")
    public void saveNoticeMessage(NoticeMessage noticeMessage) {
        Notice notice = NoticeMessage.toEntity(noticeMessage);
        Notice savedNotice = noticeRepository.save(notice);

        System.out.println(savedNotice.getId() + "번 알림 저장");
    }
}
