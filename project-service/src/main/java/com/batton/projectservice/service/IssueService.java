package com.batton.projectservice.service;

import com.batton.projectservice.client.MemberServiceFeignClient;
import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.domain.Report;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.dto.issue.GetIssueBoardResDTO;
import com.batton.projectservice.dto.issue.GetIssueResDTO;
import com.batton.projectservice.dto.issue.GetIssueInfoResDTO;
import com.batton.projectservice.dto.issue.GetMyIssueResDTO;
import com.batton.projectservice.dto.issue.PatchIssueReqDTO;
import com.batton.projectservice.dto.issue.PatchIssueBoardReqDTO;
import com.batton.projectservice.dto.issue.PostIssueReqDTO;
import com.batton.projectservice.dto.issue.*;
import com.batton.projectservice.enums.*;
import com.batton.projectservice.mq.RabbitProducer;
import com.batton.projectservice.mq.dto.NoticeMessage;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.IssueRepository;
import com.batton.projectservice.repository.ProjectRepository;
import com.batton.projectservice.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;

import static com.batton.projectservice.common.BaseResponseStatus.*;
import static com.batton.projectservice.enums.IssueStatus.*;
import static com.batton.projectservice.enums.NoticeType.BATTON;
import static com.batton.projectservice.enums.NoticeType.EXCLUDE;

@RequiredArgsConstructor
@Service
public class IssueService {
    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final BelongRepository belongRepository;
    private final ReportRepository reportRepository;
    private final MemberServiceFeignClient memberServiceFeignClient;
    private final RabbitProducer rabbitProducer;

    /**
     * 이슈 생성
     */
    @Transactional
    public Long postIssue(Long memberId, PostIssueReqDTO postIssueReqDTO) {
        Optional<Project> project = projectRepository.findById(postIssueReqDTO.getProjectId());
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(postIssueReqDTO.getProjectId(), memberId);
        List<Issue> issueList = issueRepository.findByIssueStatusOrderByIssueSeq(TODO);

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
                    issue = postIssueReqDTO.toEntity(project.get(), belong.get(), postIssueReqDTO, TODO, lastIssueSeq + 1, 1);
                } else {
                    Issue lateIssue = issueRepository.findTopByProjectIdOrderByCreatedAtDesc(project.get().getId());
                    int key = lateIssue.getIssueKey() + 1;
                    issue = postIssueReqDTO.toEntity(project.get(), belong.get(), postIssueReqDTO, TODO, lastIssueSeq + 1, key);
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
                List<Issue> issueList = issueRepository.findByIssueStatusOrderByIssueSeq(patchIssueBoardReqDTO.getAfterStatus());
                List<Belong> belongs = belongRepository.findLeader(issue.get().getProject().getId(), GradeType.LEADER);
                int preIssueNum = 0;

                // 같은 상태에서 순서를 밑으로 내리는 경우
                if (patchIssueBoardReqDTO.getIssueCase().equals(IssueCase.SPECIFIC)) {
                    // 이후 순서의 이슈들 seq 1씩 증가
                    for (int i = patchIssueBoardReqDTO.getSeqNum() + 1; i < issueList.size(); i++) {
                        issueList.get(i).updateSeq(issueList.get(i).getIssueSeq() + 1);
                    }
                    // 이전 순서의 이슈 seq
                    if (patchIssueBoardReqDTO.getSeqNum() != 0) {
                        preIssueNum = issueList.get(patchIssueBoardReqDTO.getSeqNum()).getIssueSeq();
                    }
                } else {
                    // 이슈를 완료 상태로 변경하는 권한 확인
                    if (patchIssueBoardReqDTO.getAfterStatus().equals(DONE) && belong.get().getGrade().equals(GradeType.MEMBER)) {
                        throw new BaseException(MEMBER_NO_AUTHORITY);
                    }
                    // 이후 순서의 이슈들 seq 1씩 증가
                    for (int i = patchIssueBoardReqDTO.getSeqNum(); i < issueList.size(); i++) {
                        issueList.get(i).updateSeq(issueList.get(i).getIssueSeq() + 1);
                    }
                    // 이전 순서의 이슈 seq
                    if (patchIssueBoardReqDTO.getSeqNum() != 0) {
                        preIssueNum = issueList.get(patchIssueBoardReqDTO.getSeqNum() - 1).getIssueSeq();
                    }

                    if (patchIssueBoardReqDTO.getAfterStatus().equals(REVIEW)) { // 검토 이슈 발생
                        // 소속 프로젝트의 리더들에게만 알림전송
                        for (Belong b : belongs) {
                            rabbitProducer.sendNoticeMessage(
                                    NoticeMessage.builder()
                                            .projectId(b.getProject().getId())
                                            .noticeType(NoticeType.REVIEW)
                                            .contentId(issueId)
                                            .senderId(memberId)
                                            .receiverId(b.getMemberId())
                                            .noticeContent("[" + b.getProject().getProjectTitle() + "] " + "이슈 '" + issue.get().getIssueTitle() +
                                                    "'의 상태가 검토로 변경되었습니다.")
                                            .build());
                        }
                    } else if (patchIssueBoardReqDTO.getAfterStatus().equals(DONE)) { // 이슈 승인
                        rabbitProducer.sendNoticeMessage(
                                NoticeMessage.builder()
                                        .projectId(issue.get().getProject().getId())
                                        .noticeType(NoticeType.APPROVE)
                                        .contentId(issueId)
                                        .senderId(memberId)
                                        .receiverId(issue.get().getBelong().getMemberId())
                                        .noticeContent("[" + issue.get().getProject().getProjectTitle() + "] " + "이슈 '" + issue.get().getIssueTitle() +
                                                "'의 상태가 승인 후 완료로 변경되었습니다.")
                                        .build());
                    } else if (patchIssueBoardReqDTO.getBeforeStatus().equals(REVIEW) && patchIssueBoardReqDTO.getAfterStatus().equals(PROGRESS)) {
                        rabbitProducer.sendNoticeMessage(
                                NoticeMessage.builder()
                                        .projectId(issue.get().getProject().getId())
                                        .noticeType(NoticeType.REJECT)
                                        .contentId(issueId)
                                        .senderId(memberId)
                                        .receiverId(issue.get().getBelong().getMemberId())
                                        .noticeContent("[" + issue.get().getProject().getProjectTitle() + "] " + "이슈 '" + issue.get().getIssueTitle() +
                                                "'의 상태가 반려 후 진행으로 변경되었습니다.")
                                        .build());
                    }

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
        List<Issue> issueList = issueRepository.findByProjectIdOrderByIssueSeq(projectId);
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

                if (issue.getIssueStatus().equals(TODO)) {
                    todoIssueList.add(GetIssueBoardInfoResDTO.toDTO(issue, getMemberResDTO));
                } else if (issue.getIssueStatus().equals(PROGRESS)) {
                    progressIssueList.add(GetIssueBoardInfoResDTO.toDTO(issue, getMemberResDTO));
                } else if (issue.getIssueStatus().equals(REVIEW)) {
                    reviewIssueList.add(GetIssueBoardInfoResDTO.toDTO(issue, getMemberResDTO));
                } else if (issue.getIssueStatus().equals(DONE)) {
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
                if (issue.getIssueStatus().equals(TODO)) {
                    todo = todo + 1;
                } else if (issue.getIssueStatus().equals(PROGRESS)) {
                    progress = progress + 1;
                } else if (issue.getIssueStatus().equals(REVIEW)) {
                    review = review + 1;
                } else if (issue.getIssueStatus().equals(DONE)) {
                    done = done + 1;
                }
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return new GetIssueChartResDTO(todo, progress, review, done);
    }


    /**
     * 개인 이슈 목록 조회 API
     */
    @Transactional
    public List<GetMyIssueResDTO> getMyIssue(Long memberId, IssueStatus issueStatus, String keyword) {
            List<Belong> belongList = belongRepository.findByMemberId(memberId);
            List<GetMyIssueResDTO> myIssueResDTOList = new ArrayList<>();
            List<Issue> issueList = new ArrayList<>();

            for (Belong belong : belongList) {
                if (belong.getStatus().equals(Status.ENABLED)) {
                    List<Issue> myIssueList = new ArrayList<>();
                    if (StringUtils.isEmpty(issueStatus) && StringUtils.isEmpty(keyword)) {
                        System.out.println("전체이슈");
                        // 전체 이슈 조회
                        myIssueList = issueRepository.findByBelongIdOrderByUpdatedAtDesc(belong.getId());
                    } else if (StringUtils.isEmpty(issueStatus) && !StringUtils.isEmpty(keyword)){
                        System.out.println("특정키워드");
                        // 특정 키워드 조회
                        myIssueList = issueRepository.findByBelongIdAndIssueTitleContaining(belong.getId(),keyword);
                    } else if (!StringUtils.isEmpty(issueStatus) && StringUtils.isEmpty(keyword)) {
                        System.out.println("특정이슈상태");
                        // 특정 이슈 상태 조회
                        myIssueList = issueRepository.findByBelongIdAndIssueStatus(belong.getId(), issueStatus);
                    } else {
                        System.out.println("특정키워드와 이슈상태");
                        // 특정 키워드와 이슈 상태 조회
                        myIssueList = issueRepository.findByBelongIdAndIssueStatusAndIssueTitleContaining(belong.getId(), issueStatus, keyword);
                    }

                    for (Issue issue : myIssueList) {
                        issueList.add(issue);
                    }
                } else {
                    throw new BaseException(BELONG_INVALID_ID);
                }
            }
            // 이슈 날짜 내림차순 정렬
            Collections.sort(issueList);

            for (Issue issue : issueList) {
                String updatedDate = issue.getUpdatedAt().getYear() + ". " + issue.getUpdatedAt().getMonthValue() + ". " + issue.getUpdatedAt().getDayOfMonth();
                GetMyIssueResDTO getMyIssueResDTO = GetMyIssueResDTO.toDTO(issue, updatedDate);
                myIssueResDTOList.add(getMyIssueResDTO);
            }

            return myIssueResDTOList;
    }

    /**
     * 이슈 상세 조회 API
     */
    @Transactional
    public GetIssueInfoResDTO getIssueInfo(Long memberId, Long issueId) {
        Optional<Issue> issue = issueRepository.findById(issueId);
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(issue.get().getProject().getId(), memberId);
        Optional<Report> report = reportRepository.findByIssueId(issueId);

        // 소속 유저 존재 여부 검증
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            // 이슈 존재 여부 확인
            if (issue.isPresent()) {
                GetMemberResDTO getMemberResDTO = memberServiceFeignClient.getMember(issue.get().getBelong().getMemberId());
                GetIssueInfoResDTO getIssueInfoResDTO = GetIssueInfoResDTO.toDTO(issue.get(), getMemberResDTO, report.get().getReportContent());

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
        List<Issue> issueList = issueRepository.findByIssueStatus(RELEASED);
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

    /**
     * 완료 이슈 리스트 조회 API
     */
    public List<GetIssueResDTO> getDoneIssue(Long projectId) {
        List<Issue> doneIssueList = issueRepository.findByProjectIdAndIssueStatusOrderByIssueSeq(projectId, DONE);
        List<GetIssueResDTO> issueList = new ArrayList<>();

        for(Issue issue : doneIssueList) {
            GetMemberResDTO getMemberResDTO = memberServiceFeignClient.getMember(issue.getBelong().getMemberId());
            GetIssueResDTO getIssueResDTO = GetIssueResDTO.toDTO(issue, getMemberResDTO);
            issueList.add(getIssueResDTO);
        }

        return issueList;
    }

    public String postBattonTouch(Long memberId, Long issueId, Long receiverId) {
        Optional<Issue> issue = issueRepository.findById(issueId);

        if (issue.isPresent()) {
            rabbitProducer.sendNoticeMessage(
                    NoticeMessage.builder()
                            .projectId(issue.get().getProject().getId())
                            .noticeType(BATTON)
                            .contentId(issueId)
                            .senderId(memberId)
                            .receiverId(receiverId)
                            .noticeContent("[" + issue.get().getProject().getProjectTitle() + "] " + issue.get().getBelong().getNickname() +
                                    "님께서 " + issue.get().getIssueTitle() + " 이슈를 완료하고 바톤 터치를 하였습니다.")
                            .build());
        } else {
            throw new BaseException(ISSUE_INVALID_ID);
        }

        return "이슈 바톤 터치가 완료되었습니다.";
    }
}