package com.batton.projectservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "report")
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;
    @Column(name = "report_content", columnDefinition = "TEXT")
    private String reportContent;
    @OneToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Builder
    public Report(Long id, String reportContent, Issue issue) {
        this.id = id;
        this.reportContent = reportContent;
        this.issue = issue;
    }

    public void update(String reportContent) {
        this.reportContent = reportContent;
    }
}
