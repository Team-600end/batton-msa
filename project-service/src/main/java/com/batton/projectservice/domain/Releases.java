package com.batton.projectservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "releases")
public class Releases extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "releases_id")
    private Long id;
    private int versionMajor;
    private int versionMinor;
    private int versionPatch;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    @Column(name = "release_content", columnDefinition = "TEXT")
    private String releaseContent;

    @Builder
    public Releases(Long id, int versionMajor, int versionMinor, int versionPatch, Project project, String releaseContent) {
        this.id = id;
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.versionPatch = versionPatch;
        this.project = project;
        this.releaseContent = releaseContent;
    }
}
