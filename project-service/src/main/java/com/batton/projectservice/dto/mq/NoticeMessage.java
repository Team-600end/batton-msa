package com.batton.projectservice.dto.mq;

import com.batton.projectservice.enums.NoticeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeMessage {
    private Long senderId;
    private Long receiverId;
    private Long contentId;
    private NoticeType noticeType;
    private String noticeContent;

    @Builder
    public NoticeMessage(Long senderId, Long receiverId, Long contentId, NoticeType noticeType, String noticeContent) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.contentId = contentId;
        this.noticeType = noticeType;
        this.noticeContent = noticeContent;
    }
}
