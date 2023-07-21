package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.mq.dto.NoticeMessage;
import com.batton.projectservice.mq.RabbitProducer;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.batton.projectservice.enums.NoticeType.NEW;

@RequiredArgsConstructor
@RestController
@RequestMapping("/messages")
public class TestController {
    private final RabbitProducer rabbitProducer;

    @GetMapping("/notices")
    @Operation(summary = "알림 객체 메세지 전송")
    private BaseResponse<String> sendNoticeMessage() {
        NoticeMessage noticeMessage = NoticeMessage.builder()
                .noticeContent("이승희님이 알림을 보냈습니다.")
                .noticeType(NEW)
                .senderId(1L)
                .receiverId(2L)
                .contentId(3L)
                .build();
        String result = rabbitProducer.sendNoticeMessage(noticeMessage);

        return new BaseResponse<>(result);
    }
}
