package com.batton.projectservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    //mongoDB로 변경
//    private String reportContent;

    private Long writerId;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @OneToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;


    @Builder
    public Report(Long id, String reportTitle, Long writerId, LocalDateTime createDate, LocalDateTime updateDate, Issue issue) {
        this.id = id;
        this.writerId = writerId;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.issue = issue;
    }

}
