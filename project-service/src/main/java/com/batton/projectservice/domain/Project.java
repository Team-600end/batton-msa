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
@Table(name = "project")
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    private String projectTitle;

    private String projectContent;

    @Column(unique = true)
    private String projectKey;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Belong> belongs = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Issue> issues = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Releases> releases = new ArrayList<>();

    @Builder
    public Project(Long id, String projectTitle, String projectContent, String projectKey) {
        this.id = id;
        this.projectTitle = projectTitle;
        this.projectContent = projectContent;
        this.projectKey = projectKey;
    }

}
