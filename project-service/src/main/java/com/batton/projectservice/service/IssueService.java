package com.batton.projectservice.service;

import com.batton.projectservice.client.MemberServiceFeignClient;
import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.common.IssueComparator;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.dto.issue.GetIssueBoardResDTO;
import com.batton.projectservice.dto.issue.GetIssueResDTO;
import com.batton.projectservice.dto.issue.GetIssueInfoResDTO;
import com.batton.projectservice.dto.issue.GetMyIssueResDTO;
import com.batton.projectservice.dto.issue.PatchIssueReqDTO;
import com.batton.projectservice.dto.issue.PatchIssueBoardReqDTO;
import com.batton.projectservice.dto.issue.PostIssueReqDTO;
import com.batton.projectservice.dto.issue.*;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.enums.Status;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.IssueRepository;
import com.batton.projectservice.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

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
    public Long postIssue(Long memberId, PostIssueReqDTO postIssueReqDTO) {
        Optional<Project> project = projectRepository.findById(postIssueReqDTO.getProjectId());
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(postIssueReqDTO.getProjectId(), memberId);
        List<Issue> issueList = issueRepository.findByIssueStatusOrderByIssueSeq(IssueStatus.TODO);

        // 마지막 이슈의 이슈 순서
        int lastIssueSeq = 0;
        if (issueList.size() != 0) {
            lastIssueSeq = issueList.get(issueList.size() - 1).getIssueSeq();
        }

        // 프로젝트 존재 여부 검증
        if (project.isPresent()) {
            // 소속 유저 존재 여부 검증
            if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
                Issue issue;

                // TO DO 상태의 이슈의 가장 마지막에 위치
                if (!issueRepository.existsByProjectId(project.get().getId())) {
                    issue = postIssueReqDTO.toEntity(project.get(), belong.get(), postIssueReqDTO, IssueStatus.TODO, lastIssueSeq + 1, 1);
                } else {
                    Issue lateIssue = issueRepository.findTopByProjectIdOrderByCreatedAtDesc(project.get().getId());
                    int key = lateIssue.getIssueKey() + 1;
                    issue = postIssueReqDTO.toEntity(project.get(), belong.get(), postIssueReqDTO, IssueStatus.TODO, lastIssueSeq + 1, key);
                }
                Long issueId = issueRepository.save(issue).getId();

                return issueId;
            } else {
                throw new BaseException(BELONG_INVALID_ID);
            }
        } else {
            throw new BaseException(PROJECT_INVALID_ID);
        }
    }

    /**
     * 이슈 보드 상태 및 순서 변경 API
     */
    @Transactional
    public String patchIssueBoard(Long memberId, Long issueId, PatchIssueBoardReqDTO patchIssueBoardReqDTO) {
        Optional<Issue> issue = issueRepository.findById(issueId);
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(issue.get().getProject().getId(), memberId);

        // 소속 유저 존재 여부 검증
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            // 이슈 존재 여부 검증
            if (issue.isPresent()) {
                // 이슈를 완료 상태로 변경하는 권한 확인
                if (patchIssueBoardReqDTO.getAfterStatus().equals(IssueStatus.DONE) && belong.get().getGrade().equals(GradeType.MEMBER)) {
                    throw new BaseException(MEMBER_NO_AUTHORITY);
                }
                List<Issue> issueList = issueRepository.findByIssueStatusOrderByIssueSeq(patchIssueBoardReqDTO.getAfterStatus());

                // 이후 순서의 이슈들 seq 1씩 증가
                for (int i = patchIssueBoardReqDTO.getSeqNum(); i < issueList.size(); i++) {
                    issueList.get(i).updateSeq(issueList.get(i).getIssueSeq() + 1);
                }
                int preIssueNum = 0;
                // 이전 순서의 이슈 seq
                if (patchIssueBoardReqDTO.getSeqNum() != 0) {
                    preIssueNum = issueList.get(patchIssueBoardReqDTO.getSeqNum() - 1).getIssueSeq();
                }
                // 이슈 상태, 순서 변경
                issue.get().updateIssue(preIssueNum + 1, patchIssueBoardReqDTO.getAfterStatus());
            } else {
                throw new BaseException(ISSUE_INVALID_ID);
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return "이슈 상태 변경 되었습니다.";
    }

    /**
     * 이슈 보드 목록 조회 API
     */
    @Transactional
    public GetIssueBoardResDTO getIssueBoard(Long memberId, Long projectId) {
        List<Issue> issueList = issueRepository.findByProjectId(projectId);
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);
        List<GetIssueBoardInfoResDTO> todoIssueList = new ArrayList<>();
        List<GetIssueBoardInfoResDTO> progressIssueList = new ArrayList<>();
        List<GetIssueBoardInfoResDTO> reviewIssueList = new ArrayList<>();
        List<GetIssueBoardInfoResDTO> doneIssueList = new ArrayList<>();

        // 소속 유저 존재 여부 검증
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            for (Issue issue : issueList) {
                // 이슈 담당자 이미지를 찾기 위한 통신
                GetMemberResDTO getMemberResDTO = memberServiceFeignClient.getMember(issue.getBelong().getMemberId());

                if (issue.getIssueStatus().equals(IssueStatus.TODO)) {
                    todoIssueList.add(GetIssueBoardInfoResDTO.toDTO(issue, getMemberResDTO));
                } else if (issue.getIssueStatus().equals(IssueStatus.PROGRESS)) {
                    progressIssueList.add(GetIssueBoardInfoResDTO.toDTO(issue, getMemberResDTO));
                } else if (issue.getIssueStatus().equals(IssueStatus.REVIEW)) {
                    reviewIssueList.add(GetIssueBoardInfoResDTO.toDTO(issue, getMemberResDTO));
                } else if (issue.getIssueStatus().equals(IssueStatus.DONE)) {
                    doneIssueList.add(GetIssueBoardInfoResDTO.toDTO(issue, getMemberResDTO));
                }
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return new GetIssueBoardResDTO(todoIssueList, progressIssueList, reviewIssueList, doneIssueList);
    }

    /**
     * 이슈 도넛차트 조회 API
     */
    @Transactional
    public GetIssueChartResDTO getIssueChart(Long memberId, Long projectId) {
        List<Issue> issueList = issueRepository.findByProjectId(projectId);
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);
        int todo = 0;
        int progress = 0;
        int review = 0;
        int done = 0;

        // 소속 유저 존재 여부 검증
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            for (Issue issue : issueList) {
                if (issue.getIssueStatus().equals(IssueStatus.TODO)) {
                    todo = todo + 1;
                } else if (issue.getIssueStatus().equals(IssueStatus.PROGRESS)) {
                    progress = progress + 1;
                } else if (issue.getIssueStatus().equals(IssueStatus.REVIEW)) {
                    review = review + 1;
                } else if (issue.getIssueStatus().equals(IssueStatus.DONE)) {
                    done = done + 1;
                }
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return new GetIssueChartResDTO(todo, progress, review, done);
    }


    /**
     * 내가 담당한 이슈 목록 조회 API
     */
    @Transactional
    public List<GetMyIssueResDTO> getMyIssue(Long memberId, Long projectId) {
        // 전체 이슈 목록 조회
        if (projectId == 0) {
            List<Belong> belongList = belongRepository.findByMemberId(memberId);
            List<GetMyIssueResDTO> myIssueResDTOList = new ArrayList<>();

            for (Belong belong : belongList) {
                if (belong.getStatus().equals(Status.ENABLED)) {
                    List<Issue> myIssueList = issueRepository.findByBelongIdOrderByUpdatedAtDesc(belong.getId());

                    for (Issue issue : myIssueList) {
                        String updatedDate = issue.getUpdatedAt().getYear() + ". " + issue.getUpdatedAt().getMonthValue() + ". " + issue.getUpdatedAt().getDayOfMonth();
                        GetMyIssueResDTO getMyIssueResDTO = GetMyIssueResDTO.toDTO(issue, updatedDate);
                        myIssueResDTOList.add(getMyIssueResDTO);
                    }
                } else {
                    throw new BaseException(BELONG_INVALID_ID);
                }
            }
            //이슈 날짜 정렬
            IssueComparator comp = new IssueComparator();
            Collections.sort(myIssueResDTOList, comp);

            return myIssueResDTOList;
        } else {
            // 특정 프로젝트 이슈 목록 조회
            Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);
            List<Issue> myIssueList = issueRepository.findByBelongIdOrderByUpdatedAtDesc(belong.get().getId());
            List<GetMyIssueResDTO> myIssueResDTOList = new ArrayList<>();

            // 소속 유저 존재 여부 검증
            if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
                for (Issue issue : myIssueList) {
                    String updatedDate = issue.getUpdatedAt().getYear() + ". " + issue.getUpdatedAt().getMonthValue() + ". " + issue.getUpdatedAt().getDayOfMonth();
                    GetMyIssueResDTO getMyIssueResDTO = GetMyIssueResDTO.toDTO(issue, updatedDate);
                    myIssueResDTOList.add(getMyIssueResDTO);
                }
            } else {
                throw new BaseException(BELONG_INVALID_ID);
            }

            return myIssueResDTOList;
        }
    }

    /**
     * 이슈 상세 조회 API
     */
    @Transactional
    public GetIssueInfoResDTO getIssueInfo(Long memberId, Long issueId) {
        Optional<Issue> issue = issueRepository.findById(issueId);
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(issue.get().getProject().getId(), memberId);

        // 소속 유저 존재 여부 검증
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            // 이슈 존재 여부 확인
            if (issue.isPresent()) {
                GetMemberResDTO getMemberResDTO = memberServiceFeignClient.getMember(issue.get().getBelong().getMemberId());
                GetIssueInfoResDTO getIssueInfoResDTO = GetIssueInfoResDTO.toDTO(issue.get(), getMemberResDTO);

                return getIssueInfoResDTO;
            } else {
                throw new BaseException(ISSUE_INVALID_ID);
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }
    }

    /**
     * 대시보드 이슈 목록 조회 API
     */
    public List<GetIssueResDTO> getIssueList(Long memberId, Long projectId) {
        List<Issue> issueList = issueRepository.findByProjectIdOrderByUpdatedAtDesc(projectId);
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);
        List<GetIssueResDTO> getIssueResDTOList = new ArrayList<>();

        // 소속 유저 존재 여부 검증
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            for (Issue issue : issueList) {
                GetMemberResDTO getMemberResDTO = memberServiceFeignClient.getMember(issue.getBelong().getMemberId());
                GetIssueResDTO getIssueResDTO = GetIssueResDTO.toDTO(issue, getMemberResDTO);
                getIssueResDTOList.add(getIssueResDTO);
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return getIssueResDTOList;
    }

    /**
     * 이슈 히스토리 목록 조회 API
     */
    public List<GetIssueResDTO> getIssueHistory(Long memberId, Long projectId) {
        List<Issue> issueList = issueRepository.findByIssueStatus(IssueStatus.RELEASED);
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);
        List<GetIssueResDTO> getIssueResDTOList = new ArrayList<>();

        // 소속 유저 존재 여부 검증
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            for (Issue issue : issueList) {
                GetMemberResDTO getMemberResDTO = memberServiceFeignClient.getMember(issue.getBelong().getMemberId());
                GetIssueResDTO getIssueResDTO = GetIssueResDTO.toDTO(issue, getMemberResDTO);
                getIssueResDTOList.add(getIssueResDTO);
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return getIssueResDTOList;
    }

    /**
     * 이슈 수정 API
     */
    @Transactional
    public String patchIssue(Long memberId, Long issueId, PatchIssueReqDTO patchIssueReqDTO) {
        Optional<Issue> issue = issueRepository.findById(issueId);
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(issue.get().getProject().getId(), memberId);

        // 소속 유저 존재 여부 검증
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            // 이슈 존재 여부 검증
            if (issue.isPresent()) {
                // 이슈 수정
                issue.get().modifyIssue(patchIssueReqDTO.getIssueTitle(), patchIssueReqDTO.getIssueContent(), patchIssueReqDTO.getIssueTag(), belong.get());
            } else {
                throw new BaseException(ISSUE_INVALID_ID);
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return "이슈 수정 성공";
    }

    /**
     * 이슈 삭제 API
     */
    @Transactional
    public String deleteIssue(Long memberId, Long issueId) {
        Optional<Issue> issue = issueRepository.findById(issueId);
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(issue.get().getProject().getId(), memberId);

        // 소속 유저 존재 여부 검증
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            // 이슈 존재 여부 검증
            if (issue.isPresent()) {
                issueRepository.delete(issue.get());
            } else {
                throw new BaseException(ISSUE_INVALID_ID);
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return "이슈 삭제 성공";
    }
}