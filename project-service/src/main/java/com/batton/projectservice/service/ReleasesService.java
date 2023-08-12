package com.batton.projectservice.service;

import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.*;
import com.batton.projectservice.dto.release.*;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.PublishState;
import com.batton.projectservice.repository.*;
import com.batton.projectservice.mq.RabbitProducer;
import com.batton.projectservice.mq.dto.NoticeMessage;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.IssueRepository;
import com.batton.projectservice.repository.ProjectRepository;
import com.batton.projectservice.repository.ReleasesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.batton.projectservice.common.BaseResponseStatus.*;
import static com.batton.projectservice.enums.NoticeType.*;

@RequiredArgsConstructor
@Service
public class ReleasesService {
    private final ReleasesRepository releasesRepository;
    private final ProjectRepository projectRepository;
    private final BelongRepository belongRepository;
    private final IssueRepository issueRepository;
    private final RegisteredIssueRepository registeredIssueRepository;
    private final RabbitProducer rabbitProducer;

    /**
     * 릴리즈 생성 API
     */
    @Transactional
    public Long postReleases(Long memberId, PostReleasesReqDTO postReleasesReqDTO) {
        Optional<Project> project = projectRepository.findById(postReleasesReqDTO.getProjectId());
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(postReleasesReqDTO.getProjectId(), memberId);

        // 프로젝트 존재 여부 검증
        if (project.isPresent()) {
            // 프로젝트에 소속된 리더인지 검증
            if (belong.get().getGrade() == GradeType.LEADER) {
                Releases releases = postReleasesReqDTO.toEntity(project.get(), postReleasesReqDTO, PublishState.UNPUBLISH);
                Long releaseId = releasesRepository.save(releases).getId();

                // 이슈 리스트가 존재할 경우 이슈 리스트에 릴리즈 아이디를 저장
                if (postReleasesReqDTO.getIssueList() != null) {
                    for (PostRegisteredIssueReqDTO postRegisteredIssueReqDTO : postReleasesReqDTO.getIssueList()) {
                        Optional<Issue> issue = issueRepository.findById(postRegisteredIssueReqDTO.getIssueId());
                        RegisteredIssue registeredIssue = postRegisteredIssueReqDTO.toDTO(postRegisteredIssueReqDTO, releases, issue.get());
                        registeredIssueRepository.save(registeredIssue);
                    }
                }
                return releaseId;
            } else {
                throw new BaseException(BELONG_INVALID_ID);
            }
        } else {
            throw new BaseException(PROJECT_INVALID_ID);
        }
    }

    /**
     * 릴리즈노트 발행(상태변경) API
     */
    public String patchPublish(Long memberId, Long releaseId) {
        Optional<Releases> releases = releasesRepository.findById(releaseId);

        if (releases.isPresent()) {
            Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(releases.get().getProject().getId(), memberId);
            List<Belong> belongs = belongRepository.findByProjectId(releases.get().getProject().getId());
            Releases release = releases.get();

            if (belong.isEmpty()) {
                throw new BaseException(BELONG_INVALID_ID);
            } else if (belong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            }
            releases.get().setPublishState(PublishState.PUBLISH);

            // 프로젝트에 속한 모든 구성원에게 알림 전송
            for (Belong b : belongs) {
                rabbitProducer.sendNoticeMessage(
                        NoticeMessage.builder()
                                .projectId(releases.get().getProject().getId())
                                .noticeType(NEW)
                                .contentId(releaseId)
                                .senderId(memberId)
                                .receiverId(b.getMemberId())
                                .noticeContent("[" +b.getProject().getProjectTitle() + "] " + "릴리즈노트 v" +
                                        release.getVersionMajor() + "." + release.getVersionMinor() + "." + release.getVersionPatch() +
                                        " 버전이 새로 발행되었습니다.")
                                .build());
            }
        } else {
            throw new BaseException(RELEASE_NOTE_INVALID_ID);
        }

        return "릴리즈노트가 발행되었습니다.";
    }

    /**
     * 릴리즈 노트 수정 API
     */
    public String patchReleases(Long memberId, Long releaseId, PatchReleasesReqDTO patchReleasesReqDTO) {
        Optional<Releases> releases = releasesRepository.findById(releaseId);
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(releases.get().getProject().getId(), memberId);

        // 소속 유저 존재 여부 & 리더 권한 검증
        if (belong.isPresent() && belong.get().getGrade().equals(GradeType.LEADER)) {
            // 릴리즈 노트 존재 여부 검증
            if (releases.isPresent()) {
                // 릴리즈 노트 수정
                releases.get().update(patchReleasesReqDTO.getVersionMajor(), patchReleasesReqDTO.getVersionMinor(), patchReleasesReqDTO.getVersionPatch(), patchReleasesReqDTO.getReleaseContent(), PublishState.UNPUBLISH);

                // 이슈 리스트가 존재할 경우 이슈 리스트에 릴리즈 아이디를 저장
                if (patchReleasesReqDTO.getIssueList() != null) {
                    for (PostRegisteredIssueReqDTO postRegisteredIssueReqDTO : patchReleasesReqDTO.getIssueList()) {
                        Optional<Issue> issue = issueRepository.findById(postRegisteredIssueReqDTO.getIssueId());
                        RegisteredIssue registeredIssue = postRegisteredIssueReqDTO.toDTO(postRegisteredIssueReqDTO, releases.get(), issue.get());
                        registeredIssueRepository.save(registeredIssue);
                    }
                }
            } else {
                throw new BaseException(RELEASE_NOTE_INVALID_ID);
            }

        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return "릴리즈 노트 수정 성공";
    }


    /**
     * 릴리즈노트 삭제 API
     */
    public String deleteReleases(Long memberId, Long releaseId) {
        Optional<Releases> releases = releasesRepository.findById(releaseId);

        // 릴리즈 노트 존재 여부 검증
        if (releases.isPresent()) {
            Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(releases.get().getProject().getId(), memberId);

            if (belong.isEmpty()) {
                throw new BaseException(BELONG_INVALID_ID);
            } else if (belong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            }
            releasesRepository.deleteById(releaseId);
        } else {
            throw new BaseException(RELEASE_NOTE_INVALID_ID);
        }

        return "릴리즈노트가 삭제되었습니다.";
    }

    /**
     * 릴리즈노트에 포함된 이슈 목록 조회 API
     */
    public List<GetReleasesIssueResDTO> getReleasesIssues(Long releaseId) {
        Optional<List<RegisteredIssue>> registeredIssueList = registeredIssueRepository.findByReleasesId(releaseId);
        List<GetReleasesIssueResDTO> getReleasesIssueResDTO = new ArrayList<>();

        if (registeredIssueList.isPresent()) {
            for(RegisteredIssue registeredIssue : registeredIssueList.get()) {
                Optional<Issue> issue = issueRepository.findById(registeredIssue.getIssue().getId());

                if(issue.isPresent()){
                    getReleasesIssueResDTO.add(GetReleasesIssueResDTO.toDTO(issue.get()));
                } else {
                    throw new BaseException(ISSUE_INVALID_ID);
                }
            }
        } else {
            throw new BaseException(RELEASE_NOTE_INVALID_ID);
        }

        return getReleasesIssueResDTO;
    }

    /**
     * 릴리즈노트 상세 조회 API
     */
    public GetReleasesResDTO getReleases(Long releaseId) {
        Optional<Releases> releases = releasesRepository.findById(releaseId);

        if (releases.isPresent()) {
            String publishedDate = releases.get().getUpdatedAt().getYear() + ". " + releases.get().getUpdatedAt().getMonthValue() + ". " + releases.get().getUpdatedAt().getDayOfMonth();
            List<GetReleasesIssueResDTO> issueList = getReleasesIssues(releaseId);

            GetReleasesResDTO getReleasesResDTO = GetReleasesResDTO.toDTO(releases.get(), publishedDate, issueList);

            return getReleasesResDTO;
        } else {
            throw new BaseException(RELEASE_NOTE_INVALID_ID);
        }
    }

    /**
     * 프로젝트 릴리즈 노트 조회 API (+ 릴리즈 블록)
     */
    public List<GetProjectReleasesListResDTO> getProjectReleasesList(Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        Optional<List<Releases>> releases = releasesRepository.findByProjectIdOrderByCreatedAtAsc(projectId);
        List<GetProjectReleasesListResDTO> getProjectReleasesListResDTOList = new ArrayList<>();
        String versionChanged;
        String createdDate;

        // 프로젝트 존재 여부 검증
        if (project.isPresent()) {
            // 릴리즈 노트 존재 여부 검증
            if (releases.isPresent()) {
                List<Releases> releasesList = releases.get();
                for (int i = 0; i < releasesList.size(); i++) {
                    Releases release = releasesList.get(i);

                    // 버전 변경 여부 검증
                    if (i == 0) {
                        versionChanged = "Major";
                    } else {
                        Releases previousRelease = releasesList.get(i - 1);
                        if (release.getVersionMajor() != previousRelease.getVersionMajor()) {
                            versionChanged = "Major";
                        } else if (release.getVersionMinor() != previousRelease.getVersionMinor()) {
                            versionChanged = "Minor";
                        } else {
                            versionChanged = "Patch";
                        }
                    }

                    // 이슈 태그 리스트
                    List<GetReleasesIssueResDTO> issueList = getReleasesIssues(release.getId());

                    createdDate = release.getCreatedAt().getYear() + ". " + release.getCreatedAt().getMonthValue() + ". " + release.getCreatedAt().getDayOfMonth();
                    GetProjectReleasesListResDTO getProjectReleasesListResDTO = GetProjectReleasesListResDTO.toDTO(release.getId(), versionChanged, release.getVersionMajor(), release.getVersionMinor(), release.getVersionPatch(), createdDate, issueList);
                    getProjectReleasesListResDTOList.add(getProjectReleasesListResDTO);
                }
                return getProjectReleasesListResDTOList;

            } else {
                throw new BaseException(RELEASE_NOTE_INVALID_ID);
            }
        } else {
            throw new BaseException(PROJECT_INVALID_ID);
        }
    }
}
