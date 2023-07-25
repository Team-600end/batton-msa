package com.batton.projectservice.service;

import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Report;
import com.batton.projectservice.dto.issue.PatchIssueReportReqDTO;
import com.batton.projectservice.dto.issue.PostIssueReportReqDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.repository.CommentRepository;
import com.batton.projectservice.repository.IssueRepository;
import com.batton.projectservice.repository.ReportRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.batton.projectservice.common.BaseResponseStatus.*;

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
            if (reportRepository.findByIssueId(issue.get().getId()).isPresent()) {
                throw new BaseException(ISSUE_REPORT_EXISTS);
            }
            Report report = postIssueReportReqDTO.toEntity(postIssueReportReqDTO, issue.get());
            newReport = reportRepository.save(report).getId();
        } else {
            throw new BaseException(ISSUE_INVALID_ID);
        }

        return newReport;
    }

    /**
     * 이슈 레포트 수정 API
     * */
    @Transactional
    public String modifyReport(Long reportId, PatchIssueReportReqDTO patchIssueReportReqDTO) {
        Optional<Issue> issue = issueRepository.findById(patchIssueReportReqDTO.getIssueId());
        Optional<Report> report = reportRepository.findById(reportId);

        if (issue.isPresent()) {
            if (report.isPresent()) {
                report.get().update(patchIssueReportReqDTO.getReportContent());
            } else {
                throw new BaseException(ISSUE_REPORT_INVALID_ID);
            }
        } else {
            throw new BaseException(ISSUE_INVALID_ID);
        }

        return "이슈 레포트 수정 성공";
    }

    /**
     * 이슈 레포트 삭제 API
     * */
    @Transactional
    public String removeReport(Long reportId) {
        Optional<Report> report = reportRepository.findById(reportId);

        if (report.isPresent()) {
                reportRepository.deleteById(reportId);
        } else {
            throw new BaseException(ISSUE_REPORT_INVALID_ID);
        }

        return "프로젝트 삭제 성공";
    }
}
