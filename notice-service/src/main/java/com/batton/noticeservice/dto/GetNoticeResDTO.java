package com.batton.noticeservice.dto;

import com.batton.noticeservice.domain.Notice;
import com.batton.noticeservice.enums.NoticeType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetNoticeResDTO {
    private Long contentId; // 프로젝트 id, 이슈 id, 릴리즈 노트 id 등
    private NoticeType noticeType;
    private String noticeContent;
    private String noticeDate;
    private String senderProfileImage;

    @Builder
    public GetNoticeResDTO(Long contentId, NoticeType noticeType, String noticeContent, String noticeDate, String senderProfileImage) {
        this.contentId = contentId;
        this.noticeType = noticeType;
        this.noticeContent = noticeContent;
        this.noticeDate = noticeDate;
        this.senderProfileImage = senderProfileImage;
    }

    public static GetNoticeResDTO toDTO(Notice notice, String noticeDate, String senderProfileImage) {
        return GetNoticeResDTO.builder()
                .contentId(notice.getContentId())
                .noticeType(notice.getNoticeType())
                .noticeContent(notice.getNoticeContent())
                .noticeDate(noticeDate)
                .senderProfileImage(senderProfileImage)
                .build();
    }
}
