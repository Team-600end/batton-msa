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
@Table(name = "releases")
public class Releases extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "releases_id")
    private Long id;
    private String version;
    private LocalDateTime releasesDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public Releases(Long id, String version, LocalDateTime releasesDate, Project project) {
        this.id = id;
        this.version = version;
        this.releasesDate = releasesDate;
        this.project = project;
    }
}
