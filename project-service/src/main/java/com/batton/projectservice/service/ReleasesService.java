package com.batton.projectservice.service;

import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.domain.Releases;
import com.batton.projectservice.dto.release.PostReleasesReqDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.PublishState;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.ProjectRepository;
import com.batton.projectservice.repository.ReleasesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
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
                Releases releases = postReleasesReqDTO.toEntity(project.get(), postReleasesReqDTO);
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
}
