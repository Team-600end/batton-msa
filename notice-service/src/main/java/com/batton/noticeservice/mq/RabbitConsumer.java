package com.batton.noticeservice.mq;

import com.batton.noticeservice.dto.mq.NoticeMessage;
import com.batton.noticeservice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitConsumer {
    private final NoticeRepository noticeRepository;

    @RabbitListener(queues = "notice")
    public void receiveNoticeMessage(NoticeMessage noticeMessage) {
        System.out.println(noticeMessage.getSenderId());
        System.out.println(noticeMessage.getReceiverId());
        System.out.println(noticeMessage.getContentId());
        System.out.println(noticeMessage.getNoticeType());
        System.out.println(noticeMessage.getNoticeContent());
    }
}
