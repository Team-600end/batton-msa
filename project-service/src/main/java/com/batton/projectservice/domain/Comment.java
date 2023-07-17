package com.batton.projectservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String commentContent;

    private String sendDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belong_id")
    private Belong belong;

    @Builder
    public Comment(Long id, String commentContent, String sendDate, Issue issue, Belong belong) {
        this.id = id;
        this.commentContent = commentContent;
        this.sendDate = sendDate;
        this.issue = issue;
        this.belong = belong;
    }
}
