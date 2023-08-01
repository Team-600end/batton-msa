package com.batton.projectservice.service;

import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Comment;
import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Report;
import com.batton.projectservice.dto.comment.PostCommentReqDTO;
import com.batton.projectservice.dto.report.GetIssueReportResDTO;
import com.batton.projectservice.dto.report.PatchIssueReportReqDTO;
import com.batton.projectservice.dto.report.PostIssueReportReqDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.Status;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.CommentRepository;
import com.batton.projectservice.repository.IssueRepository;
import com.batton.projectservice.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.batton.projectservice.common.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final BelongRepository belongRepository;

    /**
     * 이슈 레포트 생성 API
     * */
    @Transactional
    public Long postReport(Long memberId, PostIssueReportReqDTO postIssueReportReqDTO) {
        Optional<Issue> issue = issueRepository.findById(postIssueReportReqDTO.getIssueId());
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(issue.get().getProject().getId(), memberId);

        // 소속 유저 확인
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            Long newReport;
            // 이슈 존재 여부 확인
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
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }
    }

    /**
     * 이슈 레포트 수정 API
     * */
    @Transactional
    public String patchReport(Long memberId, Long reportId, PatchIssueReportReqDTO patchIssueReportReqDTO) {
        Optional<Issue> issue = issueRepository.findById(patchIssueReportReqDTO.getIssueId());
        Optional<Report> report = reportRepository.findById(reportId);
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(issue.get().getProject().getId(), memberId);

        // 소속 유저 확인
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            // 이슈 존재 여부 확인
            if (issue.isPresent()) {
                // 이슈 레포트 존재 여부 확인
                if (report.isPresent()) {
                    report.get().update(patchIssueReportReqDTO.getReportContent());
                } else {
                    throw new BaseException(ISSUE_REPORT_INVALID_ID);
                }
            } else {
                throw new BaseException(ISSUE_INVALID_ID);
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return "이슈 레포트 수정 성공";
    }

    /**
     * 이슈 레포트 삭제 API
     * */
    @Transactional
    public String deleteReport(Long memberId, Long reportId) {
        Optional<Report> report = reportRepository.findById(reportId);
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(report.get().getIssue().getProject().getId(), memberId);

        // 소속 유저 확인
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            // 이슈 레포트 존재 여부 확인
            if (report.isPresent()) {
                reportRepository.deleteById(reportId);
            } else {
                throw new BaseException(ISSUE_REPORT_INVALID_ID);
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return "프로젝트 삭제 성공";
    }

    /**
     * 이슈 레포트 코멘트 생성 API
     */
    @Transactional
    public String postComment(Long reportId, Long memberId, PostCommentReqDTO postCommentReqDTO) {
        Optional<Report> report = reportRepository.findById(reportId);

        // 이슈 존재 여부 확인
        if (!report.isPresent()) {
            throw new BaseException(ISSUE_REPORT_INVALID_ID);
        }
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(report.get().getIssue().getProject().getId(), memberId);

        // 소속 유저 확인
        if (belong.isPresent()) {
            // 권한 확인
            if (belong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            }
            Comment comment = postCommentReqDTO.toEntity(postCommentReqDTO, belong.get(), report.get());
            commentRepository.save(comment);

            return "코멘트 등록되었습니다";
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }
    }

    /**
     * 이슈 레포트 조회 API
     */
    public GetIssueReportResDTO getIssueReport(){


        return null;
    }
}
