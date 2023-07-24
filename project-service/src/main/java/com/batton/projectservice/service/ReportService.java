package com.batton.projectservice.service;

import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Report;
import com.batton.projectservice.dto.PostIssueReportReqDTO;
import com.batton.projectservice.repository.CommentRepository;
import com.batton.projectservice.repository.IssueRepository;
import com.batton.projectservice.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.batton.projectservice.common.BaseResponseStatus.ISSUE_INVALID_ID;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;

    /**
     * 이슈 레포트 생성 API
     * */
    @Transactional
    public Long addReport(PostIssueReportReqDTO postIssueReportReqDTO) {
        Optional<Issue> issue = issueRepository.findById(postIssueReportReqDTO.getIssueId());

        Long newReport=0L;
        if (issue.isPresent()) {
            Report report = postIssueReportReqDTO.toEntity(postIssueReportReqDTO, issue.get());
            newReport = reportRepository.save(report).getId();
        } else {
            throw new BaseException(ISSUE_INVALID_ID);
        }

        return newReport;
    }
}
