package com.batton.noticeservice.domain;

import com.batton.noticeservice.enums.NoticeType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notice")
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long contentId;
    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;
    private String noticeContent;
    private String noticeDate;

    @Builder
    public Notice(Long id, Long senderId, Long receiverId, Long contentId, NoticeType noticeType, String noticeContent, String noticeDate) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.contentId = contentId;
        this.noticeType = noticeType;
        this.noticeContent = noticeContent;
        this.noticeDate = noticeDate;
    }
}
