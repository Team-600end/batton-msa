package com.batton.memberservice.service;

import com.batton.memberservice.common.BaseException;
import com.batton.memberservice.domain.Member;
import com.batton.memberservice.dto.PostMemberReqDTO;
import com.batton.memberservice.enums.Authority;
import com.batton.memberservice.enums.Status;
import com.batton.memberservice.mq.QueueService;
import com.batton.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.batton.memberservice.common.BaseResponseStatus.*;
import static com.batton.memberservice.common.ValidationRegex.isRegexEmail;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final QueueService queueService;

    /**
     * 회원가입 API
     */
    @Transactional
    public String signupMember(PostMemberReqDTO postMemberReqDTO) {
        //이메일 정규 표현 검증
        if (!isRegexEmail(postMemberReqDTO.getEmail()))
            throw new BaseException(POST_MEMBERS_INVALID_EMAIL);

        // 이메일 존재 여부 확인
        if (memberRepository.existsByEmail(postMemberReqDTO.getEmail())) {
            log.info("signupMember 예외: " + EXIST_EMAIL_ERROR.getMessage());
            throw new BaseException(EXIST_EMAIL_ERROR);
        }
        // 비밀번호 일치 확인
        if (!postMemberReqDTO.getPassword().equals(postMemberReqDTO.getCheckPassword())) {
            log.info("signupMember 예외: " + MEMBER_PASSWORD_CONFLICT.getMessage());
            throw new BaseException(MEMBER_PASSWORD_CONFLICT);
        }
        Member member = postMemberReqDTO.toEntity(postMemberReqDTO, passwordEncoder.encode(postMemberReqDTO.getPassword()), Authority.ROLE_USER, Status.ENABLED);

        memberRepository.save(member);
        queueService.createQueueForMember(member.getId()); // 유저 Queue 생성

        return "회원가입 성공하였습니다.";
    }
}