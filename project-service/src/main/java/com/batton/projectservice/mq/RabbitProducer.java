package com.batton.projectservice.mq;

import com.batton.projectservice.dto.mq.NoticeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitProducer {
    private final RabbitTemplate rabbitTemplate;

    public String sendNoticeMessage(NoticeMessage noticeMessage) {
        rabbitTemplate.convertAndSend("notice-exchange", "notice.key.#", noticeMessage);
        return "Message sending!(Service)";
    }
}
