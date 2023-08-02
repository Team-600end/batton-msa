package com.batton.projectservice.service;

import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.domain.Releases;
import com.batton.projectservice.dto.release.GetProjectReleasesListResDTO;
import com.batton.projectservice.dto.release.PatchReleasesReqDTO;
import com.batton.projectservice.dto.release.PostReleasesReqDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.PublishState;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.ProjectRepository;
import com.batton.projectservice.repository.ReleasesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.batton.projectservice.common.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class ReleasesService {
    private final ReleasesRepository releasesRepository;
    private final ProjectRepository projectRepository;
    private final BelongRepository belongRepository;

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

            if (belong.isEmpty()) {
                throw new BaseException(BELONG_INVALID_ID);
            } else if (belong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            }

            releases.get().setPublishState(PublishState.PUBLISH);
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
                releases.get().update(patchReleasesReqDTO.getVersionMajor(), patchReleasesReqDTO.getVersionMinor(), patchReleasesReqDTO.getVersionPatch(), patchReleasesReqDTO.getReleaseContent(), patchReleasesReqDTO.getIssueList(), PublishState.UNPUBLISH);
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
     * 프로젝트 릴리즈 노트 조회 API (+ 릴리즈 블록)
     */
    public List<GetProjectReleasesListResDTO> getProjectReleasesList(Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        Optional<List<Releases>> releases = releasesRepository.findByProjectIdOrderByCreatedAtAsc(projectId);
        List<GetProjectReleasesListResDTO> getProjectReleasesListResDTOList = new ArrayList<>();
        String versionChanged;

        // 프로젝트 존재 여부 검증
        if (project.isPresent()) {
            // 릴리즈 노트 존재 여부 검증
            if (releases.isPresent()) {
                List<Releases> releasesList = releases.get();
                for (int i = 0; i < releasesList.size(); i++) {
                    Releases release = releasesList.get(i);
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

                        GetProjectReleasesListResDTO getProjectReleasesListResDTO = GetProjectReleasesListResDTO.toDTO(versionChanged, release.getVersionMajor(), release.getVersionMinor(), release.getVersionPatch(), release.getCreatedAt(), release.getIssueList());
                        getProjectReleasesListResDTOList.add(getProjectReleasesListResDTO);
                    }

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
