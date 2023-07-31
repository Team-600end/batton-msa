package com.batton.memberservice.service;

import com.batton.memberservice.common.BaseException;
import com.batton.memberservice.domain.Member;
import com.batton.memberservice.dto.GetMemberInfoResDTO;
import com.batton.memberservice.dto.PatchMemberPasswordReqDTO;
import com.batton.memberservice.dto.PatchMemberReqDTO;
import com.batton.memberservice.dto.client.GetMemberResDTO;
import com.batton.memberservice.enums.Status;
import com.batton.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

import static com.batton.memberservice.common.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 유저 정보 조회 API(Feign Client)
     * */
    public GetMemberResDTO getMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        GetMemberResDTO getMemberResDTO;

        // 유저 존재 여부 확인
        if (member.isPresent() && member.get().getStatus().equals(Status.ENABLED)) {
            getMemberResDTO = GetMemberResDTO.toDTO(member.get());
        } else {
            log.info("getMember 예외: " + MEMBER_INVALID_USER_ID.getMessage());
            throw new BaseException(MEMBER_INVALID_USER_ID);
        }

        return getMemberResDTO;
    }

    /**
     * 유저 정보 조회 API
     * */
    public GetMemberInfoResDTO getMemberInfo(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        GetMemberInfoResDTO getMemberInfoResDTO;

        // 유저 존재 여부 확인
        if (member.isPresent() && member.get().getStatus().equals(Status.ENABLED)) {
            getMemberInfoResDTO = GetMemberInfoResDTO.toDTO(member.get());
        } else {
            log.info("getMemberInfo 예외: " + MEMBER_INVALID_USER_ID.getMessage());
            throw new BaseException(MEMBER_INVALID_USER_ID);
        }

        return getMemberInfoResDTO;
    }

    /**
     * 추가할 프로젝트 멤버 정보 조회 API
     * */
    public GetMemberInfoResDTO getCheckMember(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        GetMemberInfoResDTO getMemberInfoResDTO;

        // 유저 존재 여부 확인
        if (member.isPresent() && member.get().getStatus().equals(Status.ENABLED)) {
            getMemberInfoResDTO = GetMemberInfoResDTO.toDTO(member.get());
        } else {
            log.info("getCheckMember 예외: " + MEMBER_INVALID_USER_ID.getMessage());
            throw new BaseException(MEMBER_INVALID_USER_ID);
        }

        return getMemberInfoResDTO;
    }

    /**
     * 유저 정보 수정 API
     * */
    public String patchMember(Long memberId, PatchMemberReqDTO patchMemberReqDTO) {
        Optional<Member> member = memberRepository.findById(memberId);

        // 유저 존재 여부 확인
        if (member.isPresent() && member.get().getStatus().equals(Status.ENABLED)) {
            member.get().update(patchMemberReqDTO.getNickname(), patchMemberReqDTO.getProfileImage());
        } else {
            log.info("patchMember 예외: " + MEMBER_INVALID_USER_ID.getMessage());
            throw new BaseException(MEMBER_INVALID_USER_ID);
        }

        return "회원 정보 수정되었습니다.";
    }

    /**
     * 유저 비밀번호 수정 API
     * */
    public String patchMemberPassword(Long memberId, PatchMemberPasswordReqDTO patchMemberPasswordReqDTO) {
        Optional<Member> member = memberRepository.findById(memberId);

        // 유저 존재 여부 확인
        if (member.isPresent() && member.get().getStatus().equals(Status.ENABLED)) {
            // 비밀번호 일치 여부 확인
            if (passwordEncoder.matches(patchMemberPasswordReqDTO.getCurrentPassword(), member.get().getPassword())) {
                log.info("patchMemberPassword 예외: " + MEMBER_PASSWORD_DISCORD.getMessage());
                throw new BaseException(MEMBER_PASSWORD_DISCORD);
            }
            // 비밀번호 2차 확인
            if (!patchMemberPasswordReqDTO.getChangedPassword().equals(patchMemberPasswordReqDTO.getCheckChangedPassword())) {
                log.info("patchMemberPassword 예외: " + MEMBER_PASSWORD_CONFLICT.getMessage());
                throw new BaseException(MEMBER_PASSWORD_CONFLICT);
            }
            member.get().updatePassword(passwordEncoder.encode(patchMemberPasswordReqDTO.getChangedPassword()));
        } else {
            log.info("patchMemberPassword 예외: " + MEMBER_INVALID_USER_ID.getMessage());
            throw new BaseException(MEMBER_INVALID_USER_ID);
        }

        return "회원 비밀번호 수정되었습니다.";
    }
}
