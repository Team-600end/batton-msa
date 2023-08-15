package com.batton.noticeservice.domain;

import com.batton.noticeservice.enums.NoticeType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notice")
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;
    private Long projectId;
    private Long senderId;
    private Long receiverId;
    private Long contentId;
    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;
    private String noticeContent;

    @Builder
    public Notice(Long id, Long projectId, Long senderId, Long receiverId, Long contentId, NoticeType noticeType, String noticeContent) {
        this.id = id;
        this.projectId = projectId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.contentId = contentId;
        this.noticeType = noticeType;
        this.noticeContent = noticeContent;
    }
}
