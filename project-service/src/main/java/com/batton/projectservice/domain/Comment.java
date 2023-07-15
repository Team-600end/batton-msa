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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String commentContent;

    private LocalDateTime sendDate;

    private String commentState;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Builder
    public Comment(Long id, String commentContent, LocalDateTime sendDate, String commentState, Issue issue) {
        this.id = id;
        this.commentContent = commentContent;
        this.sendDate = sendDate;
        this.commentState = commentState;
        this.issue = issue;
    }
}
