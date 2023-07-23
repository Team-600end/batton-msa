package com.batton.projectservice.service;

import com.batton.projectservice.client.MemberServiceFeignClient;
import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.dto.GetBelongResDTO;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.repository.BelongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.batton.projectservice.common.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class BelongService {
    private final BelongRepository belongRepository;
//    private final MemberServiceFeignClient memberServiceFeignClient;

    /**
     * 프로젝트 팀원 권한 변경 API
     * */
    @Transactional
    public String modifyBelong(Long memberId, Long projectId, Long belongId, GradeType grade) {
        Optional<Belong> myBelong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);

        //본인 권한 확인
        if (myBelong.isPresent()) {
            if (myBelong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(USER_NO_AUTHORITY);
            } else {
                Optional<Belong> memberBelong = belongRepository.findById(belongId);

                //해당 Belong 있는지 확인
                if (memberBelong.isPresent()) {
                    memberBelong.get().update(grade);
                } else {
                    throw new BaseException(BELONG_NOT_FOUND);
                }
            }
        } else {
            throw new BaseException(USER_NOT_FOUND);
        }

        return "프로텍트 팀원 권한 변경 성공";
    }

    /**
     * 프로젝트 팀원 조회 API
     * */
//    public GetBelongResDTO findBelong(Long memberId, Long projectId) {
//        List<Belong> belongList = belongRepository.findBelongsByProjectId(projectId, memberId);
//
//        List<GetMemberResDTO> memberList = memberServiceFeignClient.getMember(belongList.get().getMemberId());
//
//    }

    /**
     * 프로젝트 팀원 삭제 API
     * */
    @Transactional
    public String deleteTeamMember(Long memberId, Long belongId) {
        Optional<Belong> belong = belongRepository.findById(belongId);
        Optional<Belong> myBelong = belongRepository.findByProjectIdAndMemberId(belong.get().getProject().getId(), memberId);

        if (belong.isPresent()) {
            if (myBelong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(USER_NO_AUTHORITY);
            }
            belongRepository.delete(belong.get());
        } else {
            throw new BaseException(USER_NOT_FOUND);
        }

        return "프로젝트 멤버 삭제 성공";
    }
}