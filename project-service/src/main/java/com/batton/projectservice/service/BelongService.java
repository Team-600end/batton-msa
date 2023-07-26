package com.batton.projectservice.service;

import com.batton.projectservice.client.MemberServiceFeignClient;
import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.dto.belong.GetBelongResDTO;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.Status;
import com.batton.projectservice.repository.BelongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.batton.projectservice.common.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class BelongService {
    private final BelongRepository belongRepository;
    private final MemberServiceFeignClient memberServiceFeignClient;

    /**
     * 프로젝트 팀원 권한 변경 API
     * */
    @Transactional
    public String patchGrade(Long memberId, Long projectId, Long belongId, GradeType grade) {
        Optional<Belong> myBelong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);

        // 소속 확인
        if (myBelong.isPresent()) {
            // 변경 권한 확인
            if (myBelong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            } else {
                Optional<Belong> memberBelong = belongRepository.findById(belongId);

                // 소속 확인
                if (memberBelong.isPresent()) {
                    memberBelong.get().update(grade);
                } else {
                    throw new BaseException(BELONG_INVALID_ID);
                }
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return "프로텍트 팀원 권한 변경 성공";
    }

    /**
     * 프로젝트 팀원 목록 조회 API
     * */
    @Transactional
    public List<GetBelongResDTO> getBelongList(Long memberId, Long projectId) {
        List<Belong> belongList = belongRepository.findBelongsByProjectId(projectId, memberId);
        List<GetBelongResDTO> getBelongResDTOList = new ArrayList<>();

        for (Belong belong : belongList) {
            GetMemberResDTO getMemberResDTO = memberServiceFeignClient.getMember(belong.getMemberId());
            System.out.println(memberServiceFeignClient.getMember(belong.getMemberId()).getNickname());

            getBelongResDTOList.add(GetBelongResDTO.toDTO(belong, getMemberResDTO));
        }

        return getBelongResDTOList;
    }

    /**
     * 프로젝트 팀원 삭제 API
     * */
    @Transactional
    public String deleteTeamMember(Long memberId, Long belongId) {
        Optional<Belong> belong = belongRepository.findById(belongId);
        Optional<Belong> myBelong = belongRepository.findByProjectIdAndMemberId(belong.get().getProject().getId(), memberId);

        // 소속 확인
        if (belong.isPresent()) {
            // 삭제 권한 확인
            if (myBelong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            }
            belong.get().updateStatus(Status.DISABLED);
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return "프로젝트 멤버 삭제 성공";
    }
}
