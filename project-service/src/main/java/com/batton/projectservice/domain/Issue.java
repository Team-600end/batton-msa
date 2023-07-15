package com.batton.projectservice.domain;

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
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long id;

    private String issueTitle;

    private Long managerId;

    private String issueState;

    private String issueTag;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "belong_id")
    private Belong belong;;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Issue(Long id, String issueTitle, Long managerId, String issueState, String issueTag, Project project, Belong belong) {
        this.id = id;
        this.issueTitle = issueTitle;
        this.managerId = managerId;
        this.issueState = issueState;
        this.issueTag = issueTag;
        this.project = project;
        this.belong = belong;
    }



}
