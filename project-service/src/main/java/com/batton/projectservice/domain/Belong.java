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
@Table(name = "belong")
public class Belong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "belong_id")
    private Long id;

    private String grade;

    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "belong", cascade = CascadeType.REMOVE)
    private List<Issue> issues = new ArrayList<>();

    @Builder
    public Belong(Long id, String grade, Long memberId, Project project) {
        this.id = id;
        this.grade = grade;
        this.memberId = memberId;
        this.project = project;
    }
}
