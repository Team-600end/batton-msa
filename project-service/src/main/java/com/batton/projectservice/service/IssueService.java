package com.batton.projectservice.service;

import com.batton.projectservice.client.MemberServiceFeignClient;
import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.dto.issue.GetIssueBoardResDTO;
import com.batton.projectservice.dto.issue.GetIssueListResDTO;
import com.batton.projectservice.dto.issue.PatchIssueBoardReqDTO;
import com.batton.projectservice.dto.issue.PostIssueReqDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.enums.Status;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.IssueRepository;
import com.batton.projectservice.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.batton.projectservice.common.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class IssueService {
    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final BelongRepository belongRepository;
    private final MemberServiceFeignClient memberServiceFeignClient;

    /**
     * 이슈 생성
     */
    @Transactional
    public Long addIssue(PostIssueReqDTO postIssueReqDTO) {
        Optional<Project> project = projectRepository.findById(postIssueReqDTO.getProjectId());
        Optional<Belong> belong = belongRepository.findById(postIssueReqDTO.getBelongId());
        List<Issue> issues = issueRepository.findByIssueStatusOrderByIssueSeq(IssueStatus.TODO);

        // 마지막 이슈의 이슈 순서
        int lastIssueSeq = 0;
        if (issues.size() != 0) {
            lastIssueSeq = issues.get(issues.size()-1).getIssueSeq();
        }

        // 프로젝트 존재 여부 검증
        if (project.isPresent()) {
            // 소속 유저 존재 여부 검증
            if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
                Issue issue = postIssueReqDTO.toEntity(project.get(), belong.get(), postIssueReqDTO, IssueStatus.TODO, lastIssueSeq+1);
                Long issueId = issueRepository.save(issue).getId();

                return issueId;
            } else {
                throw new BaseException(BELONG_NOT_FOUND);
            }
        } else {
            throw new BaseException(PROJECT_NOT_FOUND);
        }
    }

    /**
     * 이슈 보드 상태 및 순서 변경 API
     * */
    @Transactional
    public String modifyIssueBoard(Long memberId, Long issueId, PatchIssueBoardReqDTO patchIssueBoardReqDTO) {
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(patchIssueBoardReqDTO.getProjectId(), memberId);
        Optional<Issue> issue =issueRepository.findById(issueId);

        // 소속 유저 존재 여부 검증
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            // 이슈 존재 여부 검증
            if (issue.isPresent()) {
                // 이슈를 완료 상태로 변경하는 권한 확인
                if (patchIssueBoardReqDTO.getAfterStatus().equals(IssueStatus.DONE) && belong.get().getGrade().equals(GradeType.MEMBER)) {
                    throw new BaseException(USER_NO_AUTHORITY);
                }
                List<Issue> issues = issueRepository.findByIssueStatusOrderByIssueSeq(patchIssueBoardReqDTO.getAfterStatus());

                // 이후 순서의 이슈들 seq 1씩 증가
                for (int i=patchIssueBoardReqDTO.getSeqNum(); i<issues.size(); i++) {
                    issues.get(i).updateSeq(issues.get(i).getIssueSeq()+1);
                }

                int preIssueNum = 0;
                // 이전 순서의 이슈 seq
                if (patchIssueBoardReqDTO.getSeqNum() != 0) {
                        preIssueNum = issues.get(patchIssueBoardReqDTO.getSeqNum()-1).getIssueSeq();
                }
                // 이슈 상태, 순서 변경
                issue.get().updateIssue(preIssueNum+1,patchIssueBoardReqDTO.getAfterStatus());
            } else {
                throw new BaseException(ISSUE_NOT_FOUND);
            }
        } else {
            throw new BaseException(BELONG_NOT_FOUND);
        }

        return "이슈 상태 변경 되었습니다.";
    }

    /**
     * 이슈 보드 목록 조회 API
     * */
    @Transactional
    public GetIssueBoardResDTO getIssueBoard(Long projectId) {
        List<Issue> issueList = issueRepository.findByProjectId(projectId);
        List<GetIssueListResDTO> todoIssues = new ArrayList<>();
        List<GetIssueListResDTO> progressIssues = new ArrayList<>();
        List<GetIssueListResDTO> reviewIssues = new ArrayList<>();
        List<GetIssueListResDTO> doneIssues = new ArrayList<>();

        for (Issue issue : issueList) {
            GetMemberResDTO getMemberResDTO = memberServiceFeignClient.getMember(issue.getBelong().getMemberId());

            if (issue.getIssueStatus().equals(IssueStatus.TODO)) {
                todoIssues.add(GetIssueListResDTO.toDTO(issue, getMemberResDTO));
            }
            else if (issue.getIssueStatus().equals(IssueStatus.PROGRESS)) {
                progressIssues.add(GetIssueListResDTO.toDTO(issue, getMemberResDTO));
            }
            else if (issue.getIssueStatus().equals(IssueStatus.REVIEW)) {
                reviewIssues.add(GetIssueListResDTO.toDTO(issue, getMemberResDTO));
            } else {
                doneIssues.add(GetIssueListResDTO.toDTO(issue, getMemberResDTO));
            }
        }

        return new GetIssueBoardResDTO(todoIssues, progressIssues, reviewIssues, doneIssues);
    }
}
