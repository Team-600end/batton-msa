package com.batton.memberservice.service;

import com.batton.memberservice.common.BaseException;
import com.batton.memberservice.common.BaseResponseStatus;
import com.batton.memberservice.domain.Member;
import com.batton.memberservice.dto.SignupMemberReqDTO;
import com.batton.memberservice.enums.Authority;
import com.batton.memberservice.enums.Status;
import com.batton.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.batton.memberservice.common.BaseResponseStatus.EXIST_EMAIL_ERROR;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String signupMember(SignupMemberReqDTO reqDTO) {
        if (memberRepository.existsByEmail(reqDTO.getEmail())) {
            throw new BaseException(EXIST_EMAIL_ERROR);
        }

        Member newMember = Member.builder()
                .email(reqDTO.getEmail())
                .nickname(reqDTO.getNickname())
                .password(passwordEncoder.encode(reqDTO.getPassword()))
                .authority(Authority.ROLE_USER)
                .status(Status.ENABLED)
                .build();

        memberRepository.save(newMember);

        return "회원가입 성공";
    }
}
