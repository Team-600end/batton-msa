package com.batton.memberservice.service;

import com.batton.memberservice.domain.Member;
import com.batton.memberservice.dto.MemberSignupReqDTO;
import com.batton.memberservice.enums.Authority;
import com.batton.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createMember(MemberSignupReqDTO reqDTO) {

        if (memberRepository.existsByEmail(reqDTO.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member newMember = Member.builder()
                .email(reqDTO.getEmail())
                .nickname(reqDTO.getNickname())
                .password(passwordEncoder.encode(reqDTO.getPassword()))
                .authority(Authority.ROLE_USER)
                .build();

        Long nowMemberId = memberRepository.save(newMember).getId();
    }
}
