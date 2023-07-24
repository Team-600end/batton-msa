package com.batton.noticeservice.mq.dto;

import com.batton.noticeservice.domain.Notice;
import com.batton.noticeservice.enums.NoticeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeMessage {
    private Long projectId;
    private Long senderId;
    private Long receiverId;
    private Long contentId;
    private NoticeType noticeType;
    private String noticeContent;

    @Builder
    public NoticeMessage(Long projectId, Long senderId, Long receiverId, Long contentId, NoticeType noticeType, String noticeContent) {
        this.projectId = projectId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.contentId = contentId;
        this.noticeType = noticeType;
        this.noticeContent = noticeContent;
    }

    public static Notice toEntity(NoticeMessage dto){
        return Notice.builder()
                .projectId(dto.getProjectId())
                .noticeType(dto.getNoticeType())
                .noticeContent(dto.getNoticeContent())
                .senderId(dto.getSenderId())
                .receiverId(dto.getReceiverId())
                .contentId(dto.getContentId())
                .build();
    }
}
