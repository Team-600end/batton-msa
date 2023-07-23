package com.batton.projectservice.domain;

import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.enums.IssueTag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "issue")
public class Issue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long id;
    private String issueTitle;
    private String issueContent;
    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;
    private int issueSeq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belong_id")
    private Belong belong;;
    @OneToMany(mappedBy = "issue", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Issue(Long id, String issueTitle, String issueContent, IssueStatus issueStatus, IssueTag issueTag, int issueSeq, Project project, Belong belong) {
        this.id = id;
        this.issueTitle = issueTitle;
        this.issueContent = issueContent;
        this.issueStatus = issueStatus;
        this.issueTag = issueTag;
        this.issueSeq = issueSeq;
        this.project = project;
        this.belong = belong;
    }

    public void updateSeq(int issueSeq) {
        this.issueSeq = issueSeq;
    }
    public void updateIssue(int issueSeq, IssueStatus issueStatus) {
        this.issueSeq = issueSeq;
        this.issueStatus = issueStatus;
    }
}
